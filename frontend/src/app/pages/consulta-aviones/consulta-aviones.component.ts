import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-consulta-aviones',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './consulta-aviones.component.html',
  styleUrls: ['./consulta-aviones.component.css']
})
export class ConsultaAvionesComponent {

  aerolineaSeleccionada: string = '';

  consultaRealizada = false;

  aviones: any[] = [];

  aerolineas: string[] = [
    'Avianca',
    'Copa Airlines',
    'American Airlines',
    'Delta Airlines',
    'United Airlines'
  ];

  buscarAviones() {

    if (!this.aerolineaSeleccionada) {
      alert('Debe seleccionar una aerolínea');
      return;
    }

    this.consultaRealizada = true;

    // Simulación FRONTEND
    if (this.aerolineaSeleccionada === 'United Airlines') {

      this.aviones = [];

      alert('La aerolínea consultada no tiene aviones');

      return;
    }

    this.aviones = [

      {
        modeloAvion: 'A320',
        marca: 'Airbus',
        anio: 2020,
        cantidadPasajeros: 180,
        cantidadVuelos: 340
      },

      {
        modeloAvion: 'B737',
        marca: 'Boeing',
        anio: 2018,
        cantidadPasajeros: 160,
        cantidadVuelos: 280
      },

      {
        modeloAvion: 'A350',
        marca: 'Airbus',
        anio: 2023,
        cantidadPasajeros: 300,
        cantidadVuelos: 120
      }

    ];

  }

  limpiarFiltros() {

    this.aerolineaSeleccionada = '';

    this.aviones = [];

    this.consultaRealizada = false;
  }

  nuevaConsulta() {

    this.limpiarFiltros();
  }

  imprimirPDF() {

    alert('Generando PDF...');
  }

  exportarExcel() {

    alert('Generando Excel...');
  }

}
