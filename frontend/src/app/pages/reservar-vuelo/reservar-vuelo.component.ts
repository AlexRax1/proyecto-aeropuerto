import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-reservar-vuelo',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reservar-vuelo.component.html',
  styleUrls: ['./reservar-vuelo.component.css']
})
export class ReservarVueloComponent {

  aeropuertos: string[] = [
    'Guatemala',
    'Panamá',
    'México',
    'Costa Rica',
    'Estados Unidos'
  ];

  filtros = {
    origen: '',
    destino: '',
    fechaSalida: ''
  };

  vuelosDisponibles: any[] = [];

  vueloSeleccionado: any = null;

  claseSeleccionada: string = '';

  asientoSeleccionado: string = '';

  cantidadMaletas: number | null = null;

  consultaRealizada = false;

  buscarVuelos() {

    // FA02
    if (
      !this.filtros.origen ||
      !this.filtros.destino ||
      !this.filtros.fechaSalida
    ) {

      alert('Debe ingresar los campos obligatorios');

      return;
    }

    // FA03
    if (this.filtros.origen === this.filtros.destino) {

      alert(
        'No se puede seleccionar el mismo aeropuerto de salida y llegada.'
      );

      return;
    }

    this.consultaRealizada = true;

    // FA04
    if (this.filtros.destino === 'Estados Unidos') {

      this.vuelosDisponibles = [];

      alert(
        'No se encontraron vuelos según los parámetros ingresados'
      );

      return;
    }

    // Datos simulados
    this.vuelosDisponibles = [
      {
        numeroVuelo: 'AV101',
        modelo: 'Airbus A320',
        origen: this.filtros.origen,
        destino: this.filtros.destino,
        salida: '2026-07-10 08:00',
        llegada: '2026-07-10 10:30',
        tiempo: '2h 30m',
        economica: '$250',
        ejecutiva: '$600'
      },
      {
        numeroVuelo: 'CP205',
        modelo: 'Boeing 737',
        origen: this.filtros.origen,
        destino: this.filtros.destino,
        salida: '2026-07-10 14:00',
        llegada: '2026-07-10 17:00',
        tiempo: '3h',
        economica: '$300',
        ejecutiva: '$700'
      }
    ];
  }

  seleccionarVuelo(vuelo: any) {

    this.vueloSeleccionado = vuelo;
  }

  verDetalle(vuelo: any) {

    alert(
      `Modelo: ${vuelo.modelo}
Número de vuelo: ${vuelo.numeroVuelo}
Origen: ${vuelo.origen}
Destino: ${vuelo.destino}
Salida: ${vuelo.salida}
Llegada: ${vuelo.llegada}`
    );
  }

  generarPase() {

    // Validaciones
    if (
      !this.claseSeleccionada ||
      !this.asientoSeleccionado ||
      this.cantidadMaletas === null
    ) {

      alert('Debe ingresar los campos obligatorios');

      return;
    }

    // FA06
    if (this.asientoSeleccionado === 'A1') {

      alert(
        'No se puede seleccionar el vuelo porque ya tiene vuelos asignados'
      );

      return;
    }

    alert('Pase de abordar generado correctamente');
  }

  nuevaBusqueda() {

    this.filtros = {
      origen: '',
      destino: '',
      fechaSalida: ''
    };

    this.vuelosDisponibles = [];

    this.vueloSeleccionado = null;

    this.claseSeleccionada = '';

    this.asientoSeleccionado = '';

    this.cantidadMaletas = null;

    this.consultaRealizada = false;
  }
}
