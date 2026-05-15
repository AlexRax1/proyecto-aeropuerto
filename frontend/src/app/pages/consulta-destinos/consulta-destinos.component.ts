import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-consulta-destinos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './consulta-destinos.component.html',
  styleUrls: ['./consulta-destinos.component.css']
})
export class ConsultaDestinosComponent {

  aerolineaSeleccionada: string = '';

  consultaRealizada = false;

  aerolineas: string[] = [
    'Avianca',
    'Copa Airlines',
    'American Airlines',
    'Delta Airlines',
    'United Airlines'
  ];

  destinos: any[] = [];

  buscarDestinos() {

    if (!this.aerolineaSeleccionada) {
      alert('Debe seleccionar una aerolínea');
      return;
    }

    this.consultaRealizada = true;

    // Simulación FA02
    if (this.aerolineaSeleccionada === 'Delta Airlines') {

      this.destinos = [];

      alert('La aerolínea consultada no tiene destinos autorizados');

      return;
    }

    // Datos simulados
    this.destinos = [
      {
        aeropuerto: 'Aeropuerto Internacional La Aurora',
        pais: 'Guatemala',
        ciudad: 'Ciudad de Guatemala'
      },
      {
        aeropuerto: 'Aeropuerto Internacional Juan Santamaría',
        pais: 'Costa Rica',
        ciudad: 'San José'
      },
      {
        aeropuerto: 'Aeropuerto Internacional Tocumen',
        pais: 'Panamá',
        ciudad: 'Ciudad de Panamá'
      }
    ];
  }

  limpiarFiltros() {

    this.aerolineaSeleccionada = '';

    this.destinos = [];

    this.consultaRealizada = false;
  }

  nuevaConsulta() {

    this.limpiarFiltros();
  }

  imprimirPDF() {

    alert('Generando archivo PDF...');
  }

  exportarExcel() {

    alert('Generando archivo Excel...');
  }
}
