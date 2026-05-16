import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import * as XLSX from 'xlsx';

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

  aerolineas: any[] = [];

  constructor(private http: HttpClient) {

    this.cargarAerolineas();
  }

  buscarAviones() {

    if (!this.aerolineaSeleccionada) {

      alert('Debe seleccionar una aerolínea');
      return;
    }

    this.http.get<any[]>(
      `http://localhost:8083/aviones/aerolinea/${this.aerolineaSeleccionada}`
    )
      .subscribe({

        next: (response) => {

          this.consultaRealizada = true;

          this.aviones = response;

          if (this.aviones.length === 0) {

            alert('La aerolínea consultada no tiene aviones');
          }
        },

        error: (err) => {

          console.error(err);

          alert('Error al consultar aviones');
        }

      });

  }

  cargarAerolineas() {

    this.http.get<any[]>(
      'http://localhost:8083/aerolineas'
    )
      .subscribe({

        next: (response) => {

          this.aerolineas = response;
        },

        error: (err) => {

          console.error(err);

          alert('Error al cargar aerolíneas');
        }

      });

  }

  limpiarFiltros() {

    this.aerolineaSeleccionada = '';

    this.aviones = [];

    this.consultaRealizada = false;
  }

  nuevaConsulta() {

    this.limpiarFiltros();
  }

  // ==========================
  // GENERAR PDF
  // ==========================

  imprimirPDF() {

    if (this.aviones.length === 0) {

      alert('No existen datos para exportar');
      return;
    }

    const doc = new jsPDF();

    doc.setFontSize(18);

    doc.text('Reporte de Aviones', 14, 20);

    const filas = this.aviones.map(avion => [

      avion.avionId,
      avion.marca,
      avion.ano,
      avion.cantAsientosEconomica,
      avion.cantAsientosEjecutiva,
      avion.cantVuelos,
      avion.estado

    ]);

    autoTable(doc, {

      startY: 30,

      head: [[
        'ID',
        'Marca',
        'Año',
        'Asientos Económica',
        'Asientos Ejecutiva',
        'Cantidad Vuelos',
        'Estado'
      ]],

      body: filas

    });

    doc.save('reporte-aviones.pdf');

  }

  // ==========================
  // EXPORTAR EXCEL
  // ==========================

  exportarExcel() {

    if (this.aviones.length === 0) {

      alert('No existen datos para exportar');
      return;
    }

    const datosExcel = this.aviones.map(avion => ({

      ID: avion.avionId,

      Marca: avion.marca,

      Año: avion.ano,

      'Asientos Económica': avion.cantAsientosEconomica,

      'Asientos Ejecutiva': avion.cantAsientosEjecutiva,

      'Cantidad de Vuelos': avion.cantVuelos,

      Estado: avion.estado

    }));

    const worksheet: XLSX.WorkSheet =
      XLSX.utils.json_to_sheet(datosExcel);

    const workbook: XLSX.WorkBook = {

      Sheets: {
        'Aviones': worksheet
      },

      SheetNames: ['Aviones']
    };

    XLSX.writeFile(
      workbook,
      'reporte-aviones.xlsx'
    );

  }

}
