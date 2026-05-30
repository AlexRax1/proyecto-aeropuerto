import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule, HttpParams } from '@angular/common/http';

import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

import * as XLSX from 'xlsx';
import * as FileSaver from 'file-saver';

@Component({
  selector: 'app-consulta-vuelos',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    HttpClientModule
  ],
  templateUrl: './consulta-vuelos.component.html',
  styleUrls: ['./consulta-vuelos.component.css']
})
export class ConsultaVuelosComponent {

  constructor(private http: HttpClient) {}

  filtros = {
    fechaDesde: '',
    horaDesde: '',
    fechaHasta: '',
    horaHasta: ''
  };

  vuelos: any[] = [];

  consultaRealizada = false;

  // SOLO URL BASE
  apiUrl = 'http://localhost:8080/vuelos/consulta';

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

    const params = new HttpParams()
      .set('fechaDesde', this.filtros.fechaDesde)
      .set('fechaHasta', this.filtros.fechaHasta);

    // Validar rango máximo de 30 días
    const fechaDesde = new Date(this.filtros.fechaDesde);
    const fechaHasta = new Date(this.filtros.fechaHasta);

    const diferenciaMs = fechaHasta.getTime() - fechaDesde.getTime();
    const diferenciaDias = diferenciaMs / (1000 * 60 * 60 * 24);

    if (diferenciaDias > 30) {
      alert('El rango máximo de consulta es de 30 días');
      return;
    }

    if (fechaHasta < fechaDesde) {
      alert('La fecha hasta no puede ser menor que la fecha desde');
      return;
    }

    this.http.get<any[]>(this.apiUrl, { params })
      .subscribe({

        next: (response) => {

          console.log(response);

          this.vuelos = response;

          this.consultaRealizada = true;

          if (this.vuelos.length === 0) {

            alert('No se encontraron vuelos');

          } else {

            alert('Consulta realizada correctamente');
          }
        },

        error: (error) => {

          console.error(error);

          alert('Error al consultar vuelos');
        }

      });
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

    if (this.vuelos.length === 0) {

      alert('No hay datos para imprimir');
      return;
    }

    const doc = new jsPDF();

    doc.text('Consulta de Vuelos', 14, 15);

    autoTable(doc, {

      startY: 25,

      head: [[
        'Vuelo',
        'Modelo',
        'Aerolínea',
        'Origen',
        'Destino',
        'Fecha Salida',
        'Hora Salida',
        'Fecha Llegada',
        'Hora Llegada'
      ]],

      body: this.vuelos.map(vuelo => [

        vuelo.numeroVuelo,
        vuelo.modeloAvion,
        vuelo.aerolinea,
        vuelo.origen,
        vuelo.destino,
        vuelo.fechaSalida,
        vuelo.horaSalida,
        vuelo.fechaLlegada,
        vuelo.horaLlegada

      ])

    });

    doc.save('consulta-vuelos.pdf');
  }

  exportarExcel() {

    if (this.vuelos.length === 0) {

      alert('No hay datos para exportar');
      return;
    }

    const worksheet = XLSX.utils.json_to_sheet(this.vuelos);

    const workbook = {
      Sheets: {
        'Vuelos': worksheet
      },
      SheetNames: ['Vuelos']
    };

    const excelBuffer =
      XLSX.write(workbook, {
        bookType: 'xlsx',
        type: 'array'
      });

    const data = new Blob(
      [excelBuffer],
      {
        type:
          'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8'
      }
    );

    FileSaver.saveAs(
      data,
      'consulta-vuelos.xlsx'
    );

    alert('Excel generado correctamente');
  }

}
