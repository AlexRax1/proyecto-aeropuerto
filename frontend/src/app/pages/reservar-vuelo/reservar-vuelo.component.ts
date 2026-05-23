import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient} from '@angular/common/http';
import { forkJoin, Observable } from 'rxjs';
import { SeatSelectionComponent } from '../seat-selection/seat-selection.component'; // Ajusta esta ruta según tu estructura de carpetas
import { AuthService } from '../../core/services/auth.service';

export interface Destino {
  id: number;
  labelCompleto: string;
}

export interface Vuelo {
  numeroVuelo: number;
  modelo: string;
  origen: string;
  destino: string;
  salida: string;
  llegada: string;
  tiempo: string;
  economica: string;
  ejecutiva: string;
}

@Component({
  selector: 'app-reservar-vuelo',
  standalone: true,
  imports: [CommonModule, FormsModule, SeatSelectionComponent],
  templateUrl: './reservar-vuelo.component.html',
  styleUrls: ['./reservar-vuelo.component.css']
})
export class ReservarVueloComponent implements OnInit {

  aeropuertos: Destino[] = [];
  vuelosDisponibles: Vuelo[] = [];

  filtros = {
    origen: '',
    destino: '',
    fechaSalida: ''
  };

  vueloSeleccionado: any = null;
  cantidadMaletas: number | null = null;

  consultaRealizada = false;
  mostrarModalAsientos = false; // Controla la visibilidad del modal

  asientosSeleccionados: any[] = [];
  datosPago = {
    nombre: '',
    tarjeta: '',
    vencimiento: '',
    cvv: ''
  };

  constructor(private http: HttpClient, private authService: AuthService) {}

  ngOnInit(): void {
    this.http.get<Destino[]>('http://localhost:8083/api/operaciones/destinos/select')
      .subscribe({
        next: (data) => {
          this.aeropuertos = data;
        },
        error: (err) => {
          console.error('Error al cargar destinos:', err);
          alert('Error de conexión con el servidor al cargar aeropuertos.');
        }
      });
  }

  buscarVuelos() {
    if (!this.filtros.origen || !this.filtros.destino || !this.filtros.fechaSalida) {
      alert('Debe ingresar los campos obligatorios');
      return;
    }

    if (this.filtros.origen === this.filtros.destino) {
      alert('No se puede seleccionar el mismo aeropuerto de salida y llegada.');
      return;
    }

    this.consultaRealizada = true;

    const url = `http://localhost:8083/vuelos/buscar?origenId=${this.filtros.origen}&destinoId=${this.filtros.destino}&fechaSalida=${this.filtros.fechaSalida}`;

    this.http.get<Vuelo[]>(url).subscribe({
      next: (data) => {
        this.vuelosDisponibles = data;

        if (this.vuelosDisponibles.length === 0) {
          alert('No se encontraron vuelos según los parámetros ingresados');
        }
      },
      error: (err) => {
        console.error('Error al buscar vuelos:', err);
        alert('Ocurrió un error al consultar los vuelos disponibles.');
      }
    });
  }

  seleccionarVuelo(vuelo: Vuelo) {
    this.vueloSeleccionado = vuelo;
  }

  verDetalle(vuelo: Vuelo) {
    alert(
      `Modelo: ${vuelo.modelo}\nNúmero de vuelo: ${vuelo.numeroVuelo}\nOrigen: ${vuelo.origen}\nDestino: ${vuelo.destino}\nSalida: ${vuelo.salida}\nLlegada: ${vuelo.llegada}`
    );
  }

  abrirModalAsientos() {
    if (this.cantidadMaletas === null || this.cantidadMaletas < 0) {
      alert('Debe ingresar la Cantidad de Maletas para continuar.');
      return;
    }
    this.mostrarModalAsientos = true;
  }

  cerrarModal() {
    this.mostrarModalAsientos = false;
  }

  // NUEVO: Recibe los asientos desde el modal
  onAsientosConfirmados(asientos: any[]) {
    this.asientosSeleccionados = asientos;
  }
  obtenerNombresAsientos(): string {
    return this.asientosSeleccionados.map(s => `${s.label} (${s.categoria})`).join(', ');
  }


  calcularTotal(): number {
  let total = 0;
  this.asientosSeleccionados.forEach(seat => {
    total += this.getPrecioAsiento(seat.categoria);
  });
  return total;
}

  private getPrecioAsiento(categoria: string): number {
    if (!this.vueloSeleccionado || !categoria) return 0;

    const catNormalizada = categoria.trim().toUpperCase();

    let precioString = catNormalizada === 'EJECUTIVA' 
      ? this.vueloSeleccionado.ejecutiva 
      : this.vueloSeleccionado.economica;

    if (!precioString) return 0;

    precioString = String(precioString).replace(/[^0-9.]+/g, "");

    const precio = parseFloat(precioString);
      
    return isNaN(precio) ? 0 : precio;
  }



  // ejecuta la transacción final
  procesarPago() {
    if (!this.datosPago.nombre || !this.datosPago.tarjeta || !this.datosPago.vencimiento || !this.datosPago.cvv) {
      alert("Por favor, ingrese todos los datos de la tarjeta.");
      return;
    }

    const peticionesPago: Observable<any>[] = [];

    this.asientosSeleccionados.forEach(seat => {
      const costoAsiento = this.getPrecioAsiento(seat.categoria);
      const payload = {
        vueloId: this.vueloSeleccionado.numeroVuelo,
        asientoId: seat.idAsiento,
        codigoAsiento: seat.label,
        cantMaletas: this.cantidadMaletas,
        costoBoleto: costoAsiento
      };

      peticionesPago.push(
        this.http.post('http://localhost:8080/api/reservas/crear', payload)
      );
    });

    forkJoin(peticionesPago).subscribe({
      next: (res) => {
        // RN06: Generar el pase de abordar antes de limpiar la búsqueda
        this.generarPaseDeAbordar();
        this.nuevaBusqueda(); // Limpiamos todo después de mostrar el pase
      },
      error: (err) => {
        console.error("Error al procesar el pago", err);
        const mensajeError = typeof err.error === 'string' ? err.error : "";
        
        // FA06: Ya posee vuelo para la fecha y hora seleccionada
        // Aquí asumimos que tu backend de Spring Boot devuelve este string o un código HTTP específico (ej. 409 Conflict)
        if (mensajeError.includes("ya tiene vuelos") || err.status === 409) {
          alert("No se puede seleccionar el vuelo porque ya tiene vuelos asignados");
        } else {
          // Fallback para otros errores de la API
          alert(mensajeError || "Hubo un error al procesar tu pago. Intenta nuevamente.");
        }
      }
    });
  }

  nuevaBusqueda() {
    this.filtros = { origen: '', destino: '', fechaSalida: '' };
    this.vuelosDisponibles = [];
    this.vueloSeleccionado = null;
    this.cantidadMaletas = null;
    this.consultaRealizada = false;
    this.mostrarModalAsientos = false;

    // Limpiamos también variables de pago
    this.asientosSeleccionados = [];
    this.datosPago = { nombre: '', tarjeta: '', vencimiento: '', cvv: '' };
  }

  generarPaseDeAbordar() {
    const asientosStr = this.obtenerNombresAsientos();
    
    const pase = `=================================\n` +
                 `        PASE DE ABORDAR        \n` +
                 `=================================\n` +
                 `Pasajero: ${this.datosPago.nombre}\n` +
                 `Vuelo: ${this.vueloSeleccionado.modelo} - #${this.vueloSeleccionado.numeroVuelo}\n` +
                 `Origen: ${this.vueloSeleccionado.origen}\n` +
                 `Destino: ${this.vueloSeleccionado.destino}\n` +
                 `Salida: ${this.vueloSeleccionado.salida}\n` +
                 `Llegada: ${this.vueloSeleccionado.llegada}\n` +
                 `Asiento(s): ${asientosStr}\n` +
                 `Maletas registradas: ${this.cantidadMaletas}\n` +
                 `=================================`;
                 
    alert(pase);
  }
}
