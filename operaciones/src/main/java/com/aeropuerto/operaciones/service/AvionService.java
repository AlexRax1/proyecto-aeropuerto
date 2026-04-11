    package com.aeropuerto.operaciones.service;


    import com.aeropuerto.operaciones.dto.AsientoDTO;
    import com.aeropuerto.operaciones.dto.AvionRequestDTO;
    import com.aeropuerto.operaciones.dto.EstructuraAvionDTO;
    import com.aeropuerto.operaciones.model.Aerolinea;
    import com.aeropuerto.operaciones.model.Asiento;
    import com.aeropuerto.operaciones.model.Avion;
    import com.aeropuerto.operaciones.model.ModeloAvion;
    import com.aeropuerto.operaciones.repository.AerolineaRepository;
    import com.aeropuerto.operaciones.repository.AsientoRepository;
    import com.aeropuerto.operaciones.repository.AvionRepository;
    import com.aeropuerto.operaciones.repository.ModeloAvionRepository;
    import jakarta.transaction.Transactional;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;

    import java.util.*;
    import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
    public class AvionService {

        private final AvionRepository avionRepository;
        private final AsientoRepository asientoRepository;
        private final AerolineaRepository aerolineaRepository;
        private final ModeloAvionRepository modeloAvionRepository;



        //obtener la matriz de asientos para consultar su estado y el como esta distribuido
        public EstructuraAvionDTO obtenerEstructuraAsientos(Long avionId) {

            Avion avion = avionRepository.findById(avionId)
                    .orElseThrow(() -> new RuntimeException("Avión no encontrado"));



            //listado de todos los aseintos de ese avion
            List<Asiento> asientosPlanos = asientoRepository.findByAvionId(avionId);

            // preparacion el dto de respuesta
            EstructuraAvionDTO respuesta = new EstructuraAvionDTO();
            respuesta.setIdAvion(avion.getId());
            respuesta.setIdAerolinea(avion.getAerolinea().getId());
            respuesta.setCantFilas(avion.getModeloAvion().getCantFilas());
            respuesta.setCantColumnas(avion.getModeloAvion().getCantColumnas());
            respuesta.setMapaColumnas(avion.getModeloAvion().getMapaColumnas());





            // armado de matriz de los asientos

            // Agrupamos los asientos por su número de fila (ej. todos los de la fila "1")
            Map<Integer, List<Asiento>> asientosPorFila = asientosPlanos.stream()
                    .collect(Collectors.groupingBy(a -> Integer.parseInt(a.getFila())));

            List<List<AsientoDTO>> matriz = new ArrayList<>();

            // Ordenamos las filas para que la matriz vaya de la fila 1 a la N
            List<Integer> numerosDeFila = new ArrayList<>(asientosPorFila.keySet());
            Collections.sort(numerosDeFila);

            for (Integer numFila : numerosDeFila) {
                List<Asiento> asientosEnEstaFila = asientosPorFila.get(numFila);

                // Ordenamos los asientos de la fila alfabéticamente
                asientosEnEstaFila.sort(Comparator.comparing(Asiento::getColumna));

                // Converir entidad asiento a asientoDTO
                List<AsientoDTO> filaDto = asientosEnEstaFila.stream().map(a -> {
                    AsientoDTO dto = new AsientoDTO();
                    dto.setIdAsiento(a.getId());
                    dto.setFila(a.getFila());
                    dto.setColumna(a.getColumna());
                    dto.setCategoria(a.getCategoria());
                    dto.setTipo(a.getTipo());
                    dto.setEstado(a.getEstado());
                    return dto;
                }).collect(Collectors.toList());

                matriz.add(filaDto);
            }

            respuesta.setMatrizAsientos(matriz);
            return respuesta;
        }



        @Transactional
        public Avion crearAvionConAsientos(AvionRequestDTO dto) {

            //busqueda de modelo de avión y su aerolínea para manejo de errores

            ModeloAvion modelo = modeloAvionRepository.findById(dto.getModeloAvionId())
                    .orElseThrow(() -> new RuntimeException("Modelo no encontrado"));

            Aerolinea aerolinea = aerolineaRepository.findById(dto.getAerolineaId())
                    .orElseThrow(() -> new RuntimeException("Aerolínea no encontrada"));

            // craecion y guardado del avion
            Avion nuevoAvion = new Avion();
            nuevoAvion.setAerolinea(aerolinea);
            nuevoAvion.setModeloAvion(modelo);
            nuevoAvion.setMarca(dto.getMarca());
            nuevoAvion.setAno(dto.getAno());
            nuevoAvion.setEstado("ACTIVO");
            //setear los demás campos...

            nuevoAvion = avionRepository.save(nuevoAvion); // se guarda el avion y se obtiene su id


            /*
                CREACION DE ASIENTOS DEL AVION CREADO BASADO EN SU MODELO
             */

            List<Asiento> asientosAGuardar = new ArrayList<>();

            // Un arreglo simple para mapear números a letras (0=A, 1=B, etc.)
            String[] letras = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

            for (int fila = 1; fila <= modelo.getCantFilas(); fila++) {
                for (int col = 0; col < modelo.getCantColumnas(); col++) {

                    Asiento asiento = new Asiento();
                    asiento.setAvion(nuevoAvion);
                    asiento.setFila(String.valueOf(fila));
                    asiento.setColumna(letras[col]);
                    asiento.setEstado("LIBRE"); // Estado inicial

                    // simplificado
                    asiento.setCategoria("ECONOMICA");
                    asiento.setTipo("ESTANDAR");

                    asientosAGuardar.add(asiento);
                }
            }


            asientoRepository.saveAll(asientosAGuardar);

            return nuevoAvion;
        }


    }
