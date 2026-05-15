import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-consulta-vuelos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './consulta-vuelos.component.html',
  styleUrls: ['./consulta-vuelos.component.css']
})
export class ConsultaVuelosComponent {

  filtros = {
    fechaDesde: '',
    horaDesde: '',
    fechaHasta: '',
    horaHasta: ''
  };

  vuelos: any[] = [];

  consultaRealizada = false;

  buscarVuelos() {

    if (
      !this.filtros.fechaDesde ||
      !this.filtros.horaDesde ||
      !this.filtros.fechaHasta ||
      !this.filtros.horaHasta
    ) {

      alert('Debe ingresar todos los filtros');

      return;
    }

    this.consultaRealizada = true;

    // DATOS TEMPORALES

    this.vuelos = [

      {
        numeroVuelo: 'AV245',
        modeloAvion: 'Airbus A320',
        aerolinea: 'Avianca',
        origen: 'Guatemala',
        destino: 'México',
        fechaSalida: '2026-05-10',
        horaSalida: '08:30',
        fechaLlegada: '2026-05-10',
        horaLlegada: '10:45'
      },

      {
        numeroVuelo: 'CM541',
        modeloAvion: 'Boeing 737',
        aerolinea: 'Copa Airlines',
        origen: 'Panamá',
        destino: 'Guatemala',
        fechaSalida: '2026-05-10',
        horaSalida: '12:15',
        fechaLlegada: '2026-05-10',
        horaLlegada: '14:20'
      }

    ];

    alert('Consulta realizada correctamente');
  }

  limpiarFiltros() {

    this.filtros = {
      fechaDesde: '',
      horaDesde: '',
      fechaHasta: '',
      horaHasta: ''
    };

    this.vuelos = [];

    this.consultaRealizada = false;

    alert('Filtros limpiados correctamente');
  }

  nuevaConsulta() {

    this.limpiarFiltros();

    alert('Nueva consulta iniciada');
  }

  imprimirPDF() {

    window.print();
  }

  exportarExcel() {

    alert('Aquí generarás el Excel posteriormente');
  }

}
