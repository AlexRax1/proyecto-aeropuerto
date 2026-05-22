import { CommonModule } from '@angular/common';
import {Component, OnInit} from '@angular/core';
import { FormsModule } from '@angular/forms';
import {VueloService} from '../services/vuelo.service';

@Component({
  selector: 'app-consulta-agregar-vuelo',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './consulta-agregar-vuelo.component.html',
  styleUrls: ['./consulta-agregar-vuelo.component.css']
})
export class ConsultaAgregarVueloComponent
  implements OnInit {

  aerolineaSeleccionada = 1;

  pasoActual = 1;

  aeropuertos: any[] = [];

  avionesDisponibles: any[] = [];

  tripulantesDisponibles: any[] = [];

  vuelo = {

    aeropuertoSalida: '',

    aeropuertoLlegada: '',

    fechaSalida: '',

    horaSalida: '',

    fechaLlegada: '',

    horaLlegada: '',

    avionSeleccionado: 0,

    precioEconomica: '',

    precioEjecutiva: '',

    tripulacion: [] as number[]
  };

  constructor(
    private vueloService: VueloService
  ) {}

  // =====================================================
  // INIT
  // =====================================================

  ngOnInit(): void {

    this.cargarAeropuertos();

    //this.cargarTripulacion();
  }

  // =====================================================
  // CARGAR AEROPUERTOS
  // =====================================================

  cargarAeropuertos() {

    this.vueloService
      .obtenerAeropuertos()
      .subscribe({

        next: (data) => {

          this.aeropuertos = data;
        },

        error: (err) => {

          console.error(err);

          alert(
            'Error cargando aeropuertos'
          );
        }
      });
  }

  // =====================================================
  // CARGAR AVIONES
  // =====================================================

  cargarAviones() {

    this.vueloService
      .obtenerAvionesDisponibles(

        this.aerolineaSeleccionada,

        this.vuelo.fechaSalida,

        this.vuelo.horaSalida,

        this.vuelo.fechaLlegada,

        this.vuelo.horaLlegada

      )
      .subscribe({

        next: (data: any[]) => {

          this.avionesDisponibles = data;
        },

        error: (err: any) => {

          console.error(err);

          alert(
            'No hay aviones disponibles'
          );
        }
      });

    console.log({

      aerolineaId: this.aerolineaSeleccionada,

      fechaSalida: this.vuelo.fechaSalida,

      horaSalida: this.vuelo.horaSalida,

      fechaLlegada: this.vuelo.fechaLlegada,

      horaLlegada: this.vuelo.horaLlegada
    });
  }

  // =====================================================
  // CARGAR TRIPULACIÓN
  // =====================================================

  cargarTripulacion() {

    this.vueloService.obtenerTripulacionDisponible(

      this.vuelo.fechaSalida,
      this.vuelo.horaSalida + ':00',
      this.vuelo.fechaLlegada,
      this.vuelo.horaLlegada + ':00'

    ).subscribe({

      next: (data: any[]) => {

        console.log('TRIPULACION:', data);

        this.tripulantesDisponibles = data;
      },

      error: (err: any) => {

        console.error(err);

        alert('Error cargando tripulación');
      }
    });
  }


  // =====================================================
  // PASO 1
  // =====================================================

  siguientePaso() {

    if (
      !this.vuelo.aeropuertoSalida ||
      !this.vuelo.aeropuertoLlegada ||
      !this.vuelo.fechaSalida ||
      !this.vuelo.horaSalida ||
      !this.vuelo.fechaLlegada ||
      !this.vuelo.horaLlegada
    ) {

      alert(
        'Debe ingresar los campos obligatorios'
      );

      return;
    }

    if (
      this.vuelo.aeropuertoSalida ===
      this.vuelo.aeropuertoLlegada
    ) {

      alert(
        'No se puede seleccionar el mismo aeropuerto de salida y llegada.'
      );

      return;
    }

    const salida = new Date(
      `${this.vuelo.fechaSalida}T${this.vuelo.horaSalida}`
    );

    const llegada = new Date(
      `${this.vuelo.fechaLlegada}T${this.vuelo.horaLlegada}`
    );

    if (llegada <= salida) {

      alert(
        'La fecha y hora de llegada debe ser mayor a la fecha y hora de salida.'
      );

      return;
    }

    const ahora = new Date();

    const diferenciaHoras =
      (salida.getTime() - ahora.getTime())
      / (1000 * 60 * 60);

    if (diferenciaHoras < 5) {

      alert(
        'Tiempo mínimo para la preparación 5 horas a partir de la hora actual.'
      );

      return;
    }

    // ============================================
    // CARGAR AVIONES DISPONIBLES
    // ============================================

    this.cargarAviones();

    this.pasoActual = 2;
  }

  // =====================================================
  // SELECCIONAR AVIÓN
  // =====================================================

  seleccionarAvion(id: number) {

    this.vuelo.avionSeleccionado = id;
  }

  // =====================================================
  // IR A TRIPULACIÓN
  // =====================================================

  irTripulacion() {

    if (!this.vuelo.avionSeleccionado) {

      alert('Debe seleccionar un avión');

      return;
    }

    if (
      !this.vuelo.precioEconomica ||
      !this.vuelo.precioEjecutiva
    ) {

      alert('Debe ingresar los precios');

      return;
    }
    this.cargarTripulacion();
    this.pasoActual = 3;
  }

  // =====================================================
  // TOGGLE TRIPULANTE
  // =====================================================

  toggleTripulante(id: number) {

    const index =
      this.vuelo.tripulacion.indexOf(id);

    if (index >= 0) {

      this.vuelo.tripulacion.splice(index, 1);

    } else {

      this.vuelo.tripulacion.push(id);
    }
  }

  // =====================================================
  // GUARDAR VUELO
  // =====================================================

  guardarVuelo() {

    if (
      this.vuelo.tripulacion.length === 0
    ) {

      alert(
        'Debe seleccionar tripulación'
      );

      return;
    }

    const payload = {

      avionId:
      this.vuelo.avionSeleccionado,

      origen:
      this.vuelo.aeropuertoSalida,

      destino:
      this.vuelo.aeropuertoLlegada,

      fechaSalida:
      this.vuelo.fechaSalida,

      horaSalida:
      this.vuelo.horaSalida,

      fechaLlegada:
      this.vuelo.fechaLlegada,

      horaLlegada:
      this.vuelo.horaLlegada,

      precioEconomica:
      this.vuelo.precioEconomica,

      precioEjecutiva:
      this.vuelo.precioEjecutiva,

      tripulacion:
      this.vuelo.tripulacion,

      usuario:
        'admin'
    };

    this.vueloService
      .crearVuelo(payload)
      .subscribe({

        next: () => {

          alert(
            'Vuelo creado exitosamente'
          );

          this.nuevoVuelo();
        },

        error: (err) => {

          console.error(err);

          alert(
            err.error.message ||
            'Error al crear vuelo'
          );
        }
      });
  }

  // =====================================================
  // NUEVO VUELO
  // =====================================================

  nuevoVuelo() {

    this.vuelo = {

      aeropuertoSalida: '',

      aeropuertoLlegada: '',

      fechaSalida: '',

      horaSalida: '',

      fechaLlegada: '',

      horaLlegada: '',

      avionSeleccionado: 0,

      precioEconomica: '',

      precioEjecutiva: '',

      tripulacion: []
    };

    this.pasoActual = 1;
  }

  // =====================================================
  // CANCELAR
  // =====================================================

  cancelar() {

    this.nuevoVuelo();

    alert(
      'Operación cancelada'
    );
  }
}
