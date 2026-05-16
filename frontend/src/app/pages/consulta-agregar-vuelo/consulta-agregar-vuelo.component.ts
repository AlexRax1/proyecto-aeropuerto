import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-consulta-agregar-vuelo',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './consulta-agregar-vuelo.component.html',
  styleUrls: ['./consulta-agregar-vuelo.component.css']
})
export class ConsultaAgregarVueloComponent {

  pasoActual = 1;

  vuelo = {
    aeropuertoSalida: '',
    aeropuertoLlegada: '',
    fechaSalida: '',
    horaSalida: '',
    fechaLlegada: '',
    horaLlegada: '',
    avionSeleccionado: '',
    precioEconomica: '',
    precioEjecutiva: '',
    tripulacion: [] as string[]
  };

  aeropuertos = [
    'Guatemala',
    'México',
    'Madrid',
    'Los Ángeles',
    'Panamá'
  ];

  avionesDisponibles = [
    {
      id: 'AV-100',
      modelo: 'Boeing 737',
      capacidad: 180,
      estado: 'Activo'
    },
    {
      id: 'AV-200',
      modelo: 'Airbus A320',
      capacidad: 150,
      estado: 'Activo'
    }
  ];

  tripulantesDisponibles = [
    'Juan Pérez',
    'María López',
    'Carlos Méndez',
    'Ana García',
    'Luis Ramírez'
  ];

  siguientePaso() {

    if (
      !this.vuelo.aeropuertoSalida ||
      !this.vuelo.aeropuertoLlegada ||
      !this.vuelo.fechaSalida ||
      !this.vuelo.horaSalida ||
      !this.vuelo.fechaLlegada ||
      !this.vuelo.horaLlegada
    ) {

      alert('Debe ingresar los campos obligatorios');
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
      (salida.getTime() - ahora.getTime()) /
      (1000 * 60 * 60);

    if (diferenciaHoras < 5) {

      alert(
        'Tiempo mínimo para la preparación 5 horas a partir de la hora actual.'
      );

      return;
    }

    this.pasoActual = 2;
  }

  seleccionarAvion(id: string) {

    this.vuelo.avionSeleccionado = id;
  }

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

    this.pasoActual = 3;
  }

  toggleTripulante(nombre: string) {

    const index =
      this.vuelo.tripulacion.indexOf(nombre);

    if (index >= 0) {

      this.vuelo.tripulacion.splice(index, 1);

    } else {

      this.vuelo.tripulacion.push(nombre);
    }
  }

  guardarVuelo() {

    if (this.vuelo.tripulacion.length === 0) {

      alert('Debe seleccionar tripulación');
      return;
    }

    console.log('Vuelo guardado:', this.vuelo);

    alert('Vuelo creado exitosamente');

    this.nuevoVuelo();
  }

  nuevoVuelo() {

    this.vuelo = {
      aeropuertoSalida: '',
      aeropuertoLlegada: '',
      fechaSalida: '',
      horaSalida: '',
      fechaLlegada: '',
      horaLlegada: '',
      avionSeleccionado: '',
      precioEconomica: '',
      precioEjecutiva: '',
      tripulacion: []
    };

    this.pasoActual = 1;
  }

  cancelar() {

    this.nuevoVuelo();

    alert('Operación cancelada');
  }
}
