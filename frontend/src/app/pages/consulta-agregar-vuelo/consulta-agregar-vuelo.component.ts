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

  templateUrl:
    './consulta-agregar-vuelo.component.html',

  styleUrls: [
    './consulta-agregar-vuelo.component.css'
  ]
})

export class ConsultaAgregarVueloComponent
  implements OnInit {

  // =====================================================
  // VARIABLES
  // =====================================================

  aerolineaSeleccionada = 1;

  pasoActual = 1;

  aeropuertos: any[] = [];

  avionesDisponibles: any[] = [];

  paquetesTripulacion: any[] = [];

  // =====================================================
  // MODELO VUELO
  // =====================================================

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

    paqueteTripulacionId: 0
  };

  constructor(
    private vueloService: VueloService
  ) {}

  // =====================================================
  // INIT
  // =====================================================

  ngOnInit(): void {

    this.cargarAeropuertos();
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

          console.log(
            'AVIONES:',
            data
          );

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

      aerolineaId:
      this.aerolineaSeleccionada,

      fechaSalida:
      this.vuelo.fechaSalida,

      horaSalida:
      this.vuelo.horaSalida,

      fechaLlegada:
      this.vuelo.fechaLlegada,

      horaLlegada:
      this.vuelo.horaLlegada
    });
  }

  // =====================================================
  // CARGAR PAQUETES
  // =====================================================

  cargarPaquetesTripulacion() {

    this.vueloService
      .obtenerPaquetesTripulacion()
      .subscribe({

        next: (data: any[]) => {

          console.log(
            'PAQUETES:',
            data
          );

          this.paquetesTripulacion = data;
        },

        error: (err: any) => {

          console.error(err);

          alert(
            'Error cargando paquetes de tripulación'
          );
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

    const hoy = new Date();

    hoy.setHours(0, 0, 0, 0);

    const fechaSalida = new Date(
      this.vuelo.fechaSalida
    );

    if (fechaSalida < hoy) {

      alert(
        'No se pueden crear vuelos con fechas anteriores al día de hoy.'
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

    // =====================================================
    // CARGAR AVIONES
    // =====================================================

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

      alert(
        'Debe seleccionar un avión'
      );

      return;
    }

    if (

      !this.vuelo.precioEconomica ||

      !this.vuelo.precioEjecutiva

    ) {

      alert(
        'Debe ingresar los precios'
      );

      return;
    }

    // =====================================================
    // CARGAR PAQUETES
    // =====================================================

    this.cargarPaquetesTripulacion();

    this.pasoActual = 3;
  }

  // =====================================================
  // GUARDAR VUELO
  // =====================================================

  guardarVuelo() {

    if (
      !this.vuelo.paqueteTripulacionId
    ) {

      alert(
        'Debe seleccionar un paquete de tripulación'
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

      tripulacionId:
      this.vuelo.paqueteTripulacionId,

      usuario:
        'admin'
    };

    console.log(
      'PAYLOAD:',
      payload
    );

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

            err.error?.message ||

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

      paqueteTripulacionId: 0
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
