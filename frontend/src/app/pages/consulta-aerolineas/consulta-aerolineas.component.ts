import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-consulta-aerolineas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './consulta-aerolineas.component.html',
  styleUrls: ['./consulta-aerolineas.component.css']
})
export class ConsultaAerolineasComponent {

  aeropuertoSeleccionado: string = '';

  consultaRealizada = false;

  aerolineas: any[] = [];

  aeropuertos: string[] = [
    'Aeropuerto Internacional La Aurora',
    'Aeropuerto Internacional El Salvador',
    'Aeropuerto Internacional Juan Santamaría',
    'Aeropuerto Internacional Tocumen',
    'Aeropuerto Internacional de Cancún'
  ];

  buscarAerolineas() {

    if (!this.aeropuertoSeleccionado) {
      alert('Debe seleccionar un aeropuerto');
      return;
    }

    this.consultaRealizada = true;

    // Simulación temporal FRONTEND
    if (this.aeropuertoSeleccionado === 'Aeropuerto Internacional Tocumen') {

      this.aerolineas = [];

      alert('El aeropuerto consultado no tiene aerolíneas');

      return;
    }

    this.aerolineas = [

      {
        nombreAerolinea: 'Avianca',
        cantidadAviones: 45,
        destinosAutorizados: 120
      },

      {
        nombreAerolinea: 'Copa Airlines',
        cantidadAviones: 60,
        destinosAutorizados: 95
      },

      {
        nombreAerolinea: 'American Airlines',
        cantidadAviones: 80,
        destinosAutorizados: 150
      }

    ];

  }

  limpiarFiltros() {

    this.aeropuertoSeleccionado = '';

    this.aerolineas = [];

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
