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

            //"""ModeloAvion modelo = modeloAvionRepository.findById(dto.getModeloAvionId()).orElseThrow(() -> new RuntimeException("Modelo no encontrado"));

            Aerolinea aerolinea = aerolineaRepository.findById(dto.getAerolineaId())
                    .orElseThrow(() -> new RuntimeException("Aerolínea no encontrada"));


            //"""
            int totalAsientosRequeridos = dto.getCantAsientosEjecutiva() + dto.getCantAsientosEconomica();
            int totalAsientosCuadricula = dto.getCantFilas() * dto.getCantColumnas();
            if (totalAsientosRequeridos != totalAsientosCuadricula) {
                throw new RuntimeException("La suma de asientos ejecutiva y económica (" + totalAsientosRequeridos + ") no coincide con la cuadrícula de " + dto.getCantFilas() + "x" + dto.getCantColumnas() + " (" + totalAsientosCuadricula + ")");
            }

            //crear y guardar el modelo de avion """"
            ModeloAvion nuevoModelo = new ModeloAvion();
            nuevoModelo.setNombre(dto.getNombreModelo());
            nuevoModelo.setCantFilas(dto.getCantFilas());
            nuevoModelo.setCantColumnas(dto.getCantColumnas());
            nuevoModelo.setMapaColumnas(dto.getMapaColumnas());
            nuevoModelo = modeloAvionRepository.save(nuevoModelo);

            // craecion y guardado del avion
            Avion nuevoAvion = new Avion();
            nuevoAvion.setAerolinea(aerolinea);
            nuevoAvion.setModeloAvion(nuevoModelo);//""""
            nuevoAvion.setMarca(dto.getMarca());
            nuevoAvion.setAno(dto.getAno());
            nuevoAvion.setCantAsientosEjecutiva(dto.getCantAsientosEjecutiva());//'''
            nuevoAvion.setCantAsientosEconomica(dto.getCantAsientosEconomica());//'''
            nuevoAvion.setEstado("ACTIVO");
            nuevoAvion = avionRepository.save(nuevoAvion); // se guarda el avion y se obtiene su id


            List<Asiento> asientosAGuardar = new ArrayList<>();


            // Un arreglo simple para mapear números a letras (0=A, 1=B, etc.)
            String letras = dto.getMapaColumnas().replace("-", "");//'''
            int contadorEjecutiva = 0;

            for (int fila = 1; fila <= nuevoModelo.getCantFilas(); fila++) {
                for (int col = 0; col < nuevoModelo.getCantColumnas(); col++) {

                    Asiento asiento = new Asiento();
                    asiento.setAvion(nuevoAvion);
                    asiento.setFila(String.valueOf(fila));

                    char letraColumna = letras.charAt(col);
                    asiento.setColumna(String.valueOf(letraColumna));

                    asiento.setEstado("ACTIVO"); // manejar luego


                    // categorias
                    if (contadorEjecutiva < dto.getCantAsientosEjecutiva()) {
                        asiento.setCategoria("EJECUTIVA");
                        contadorEjecutiva++;
                    } else {
                        asiento.setCategoria("ECONOMICA");
                    }
                    //si es ventana/pasillo/medio
                    String tipoAsiento = determinarTipoAsiento(letraColumna, dto.getMapaColumnas(), letras);
                    asiento.setTipo(tipoAsiento);

                    asientosAGuardar.add(asiento);
                }
            }


            asientoRepository.saveAll(asientosAGuardar);

            return nuevoAvion;
        }


        // calcular si es asiento pasillo/ventana/medio
        private String determinarTipoAsiento(char letra, String mapaOriginal, String letrasLimpias) {
            //ventana si es la primera o la ultima letra

            if (letra == letrasLimpias.charAt(0) || letra == letrasLimpias.charAt(letrasLimpias.length() - 1)) {
                return "VENTANA";
            }

            // pasillo si el el mapa "ABC-DEF" la letra tiene un guion al lado
            int indexEnMapa = mapaOriginal.indexOf(letra);
            if (indexEnMapa > 0 && mapaOriginal.charAt(indexEnMapa - 1) == '-') {
                return "PASILLO";
            }
            if (indexEnMapa < mapaOriginal.length() - 1 && mapaOriginal.charAt(indexEnMapa + 1) == '-') {
                return "PASILLO";
            }

            // si no cumple es el de medio
            return "MEDIO";
        }


    }
