import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import * as XLSX from 'xlsx';

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

  destinos: any[] = [];

  aerolineas: any[] = [];

  constructor(private http: HttpClient) {

    this.cargarAerolineas();
  }

  // ==========================
  // CARGAR AEROLÍNEAS
  // ==========================

  cargarAerolineas() {

    this.http.get<any[]>(
      'http://localhost:8080/consulta-destinos/aerolineas'
    )
      .subscribe({

        next: (response) => {

          console.log('Aerolíneas cargadas', response);

          this.aerolineas = response;
        },

        error: (err) => {

          console.error(err);

          alert('Error al cargar aerolíneas');
        }

      });

  }

  // ==========================
  // BUSCAR DESTINOS
  // ==========================

  buscarDestinos() {

    if (!this.aerolineaSeleccionada) {

      alert('Debe seleccionar una aerolínea');
      return;
    }

    this.http.get<any[]>(

      `http://localhost:8080/consulta-destinos/${this.aerolineaSeleccionada}`

    ).subscribe({

        next: (response) => {

          console.log('Destinos encontrados', response);

          this.consultaRealizada = true;

          this.destinos = response;

          if (this.destinos.length === 0) {

            alert(
              'La aerolínea consultada no tiene destinos autorizados'
            );
          }
        },

        error: (err) => {

          console.error(err);

          alert('Error al consultar destinos');
        }

      });

  }

  // ==========================
  // LIMPIAR
  // ==========================

  limpiarFiltros() {

    this.aerolineaSeleccionada = '';

    this.destinos = [];

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

    if (this.destinos.length === 0) {

      alert('No existen datos para exportar');
      return;
    }

    const doc = new jsPDF();

    doc.setFontSize(18);

    doc.text('Reporte de Destinos Autorizados', 14, 20);

    const filas = this.destinos.map(destino => [

      destino.nombreAeropuerto,
      destino.paisDestino,
      destino.ciudadDestino

    ]);

    autoTable(doc, {

      startY: 30,

      head: [[
        'Nombre Aeropuerto',
        'País',
        'Ciudad'
      ]],

      body: filas

    });

    doc.save('reporte-destinos.pdf');

  }

  // ==========================
  // EXPORTAR EXCEL
  // ==========================

  exportarExcel() {

    if (this.destinos.length === 0) {

      alert('No existen datos para exportar');
      return;
    }

    const datosExcel = this.destinos.map(destino => ({

      'Nombre Aeropuerto': destino.nombreAeropuerto,

      'País': destino.paisDestino,

      'Ciudad': destino.ciudadDestino

    }));

    const worksheet: XLSX.WorkSheet =
      XLSX.utils.json_to_sheet(datosExcel);

    const workbook: XLSX.WorkBook = {

      Sheets: {
        'Destinos': worksheet
      },

      SheetNames: ['Destinos']
    };

    XLSX.writeFile(
      workbook,
      'reporte-destinos.xlsx'
    );

  }

}
