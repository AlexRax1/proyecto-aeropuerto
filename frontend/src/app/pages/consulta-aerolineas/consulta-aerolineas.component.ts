import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import * as XLSX from 'xlsx';

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

  // Ahora los aeropuertos vienen del backend
  aeropuertos: any[] = [];

  constructor(private http: HttpClient) {

    this.cargarAeropuertos();
  }

  // ==========================
  // CARGAR AEROPUERTOS
  // ==========================

  cargarAeropuertos() {

    this.http.get<any[]>(
      'http://localhost:8083/aeropuertos'
    )
      .subscribe({

        next: (response) => {

          console.log('Aeropuertos cargados', response);

          this.aeropuertos = response;
        },

        error: (err) => {

          console.error(err);

          alert('Error al cargar aeropuertos');
        }

      });

  }

  // ==========================
  // BUSCAR AEROLÍNEAS
  // ==========================

  buscarAerolineas() {

    if (!this.aeropuertoSeleccionado) {

      alert('Debe seleccionar un aeropuerto');
      return;
    }

    this.http.get<any[]>(
      `http://localhost:8083/consulta-aerolineas/${this.aeropuertoSeleccionado}`
    )
      .subscribe({

        next: (response) => {

          console.log('Aerolíneas encontradas', response);

          this.consultaRealizada = true;

          this.aerolineas = response;

          if (this.aerolineas.length === 0) {

            alert('El aeropuerto consultado no tiene aerolíneas');
          }

        },

        error: (err) => {

          console.error(err);

          alert('Error al consultar aerolíneas');
        }

      });

  }

  // ==========================
  // LIMPIAR FILTROS
  // ==========================

  limpiarFiltros() {

    this.aeropuertoSeleccionado = '';

    this.aerolineas = [];

    this.consultaRealizada = false;
  }

  // ==========================
  // NUEVA CONSULTA
  // ==========================

  nuevaConsulta() {

    this.limpiarFiltros();
  }

  // ==========================
  // GENERAR PDF
  // ==========================

  imprimirPDF() {

    if (this.aerolineas.length === 0) {

      alert('No existen datos para exportar');
      return;
    }

    const doc = new jsPDF();

    doc.setFontSize(18);

    doc.text('Reporte de Aerolíneas', 14, 20);

    doc.setFontSize(12);

    doc.text(
      `Aeropuerto: ${this.aeropuertoSeleccionado}`,
      14,
      30
    );

    const filas = this.aerolineas.map(aerolinea => [

      aerolinea.nombreAerolinea,

      aerolinea.cantidadAviones,

      aerolinea.destinosAutorizados

    ]);

    autoTable(doc, {

      startY: 40,

      head: [[

        'Nombre Aerolínea',

        'Cantidad Aviones',

        'Destinos Autorizados'

      ]],

      body: filas

    });

    doc.save('reporte-aerolineas.pdf');

  }

  // ==========================
  // EXPORTAR EXCEL
  // ==========================

  exportarExcel() {

    if (this.aerolineas.length === 0) {

      alert('No existen datos para exportar');
      return;
    }

    const datosExcel = this.aerolineas.map(aerolinea => ({

      'Nombre Aerolínea': aerolinea.nombreAerolinea,

      'Cantidad Aviones': aerolinea.cantidadAviones,

      'Destinos Autorizados': aerolinea.destinosAutorizados

    }));

    const worksheet: XLSX.WorkSheet =
      XLSX.utils.json_to_sheet(datosExcel);

    const workbook: XLSX.WorkBook = {

      Sheets: {

        'Aerolíneas': worksheet
      },

      SheetNames: ['Aerolíneas']

    };

    XLSX.writeFile(
      workbook,
      'reporte-aerolineas.xlsx'
    );

  }

}
