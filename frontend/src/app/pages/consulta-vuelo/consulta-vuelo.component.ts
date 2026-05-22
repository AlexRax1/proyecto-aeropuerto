import { Component } from '@angular/core';

import { CommonModule } from '@angular/common';

import { FormsModule } from '@angular/forms';

import {
  HttpClient,
  HttpClientModule
} from '@angular/common/http';

import jsPDF from 'jspdf';

import autoTable from 'jspdf-autotable';

import * as XLSX from 'xlsx';

import * as FileSaver from 'file-saver';

@Component({
  selector: 'app-consulta-vuelo',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    HttpClientModule
  ],
  templateUrl: './consulta-vuelo.component.html',
  styleUrls: ['./consulta-vuelo.component.css']
})
export class ConsultaVueloComponent {

  constructor(
    private http: HttpClient
  ) {}

  // =========================================
  // NÚMERO DE VUELO
  // =========================================

  numeroVuelo: string = '';

  // =========================================
  // DATOS DEL VUELO
  // =========================================

  vuelo: any = null;

  // =========================================
  // CONTROL DE CONSULTA
  // =========================================

  consultaRealizada = false;

  // =========================================
  // API
  // =========================================

  apiUrl =
    'http://localhost:8083/vuelos/consulta-vuelo';

  // =========================================
  // BUSCAR VUELO
  // =========================================

  buscarVuelo() {

    if (!this.numeroVuelo) {

      alert(
        'Debe ingresar el número de vuelo'
      );

      return;
    }

    this.http.get<any>(
      `${this.apiUrl}/${this.numeroVuelo}`
    ).subscribe({

      next: (response) => {

        console.log(response);

        this.vuelo = response;

        this.consultaRealizada = true;

        alert(
          'Consulta realizada correctamente'
        );
      },

      error: (error) => {

        console.error(error);

        this.vuelo = null;

        this.consultaRealizada = false;

        alert(
          'El número de vuelo ingresado no se encontró'
        );
      }

    });
  }

  // =========================================
  // LIMPIAR
  // =========================================

  limpiar() {

    this.numeroVuelo = '';

    this.vuelo = null;

    this.consultaRealizada = false;

    alert(
      'Consulta limpiada correctamente'
    );
  }

  // =========================================
  // NUEVA CONSULTA
  // =========================================

  nuevaConsulta() {

    this.limpiar();

    alert(
      'Nueva consulta iniciada'
    );
  }

  // =========================================
  // PDF
  // =========================================

  imprimirPDF() {

    if (!this.vuelo) {

      alert(
        'No hay información para imprimir'
      );

      return;
    }

    const doc = new jsPDF();

    doc.text(
      'Consulta de Vuelo',
      14,
      15
    );

    autoTable(doc, {

      startY: 25,

      head: [[
        'Número Vuelo',
        'Modelo',
        'Aerolínea',
        'Origen',
        'Destino',
        'Fecha Salida',
        'Hora Salida',
        'Fecha Llegada',
        'Hora Llegada'
      ]],

      body: [[

        this.vuelo.numeroVuelo,
        this.vuelo.modeloAvion,
        this.vuelo.aerolinea,
        this.vuelo.origen,
        this.vuelo.destino,
        this.vuelo.fechaSalida,
        this.vuelo.horaSalida,
        this.vuelo.fechaLlegada,
        this.vuelo.horaLlegada

      ]]

    });

    doc.save(
      'consulta-vuelo.pdf'
    );
  }

  // =========================================
  // EXCEL
  // =========================================

  exportarExcel() {

    if (!this.vuelo) {

      alert(
        'No hay información para exportar'
      );

      return;
    }

    const datos = [{
      numeroVuelo:
      this.vuelo.numeroVuelo,

      modeloAvion:
      this.vuelo.modeloAvion,

      aerolinea:
      this.vuelo.aerolinea,

      origen:
      this.vuelo.origen,

      destino:
      this.vuelo.destino,

      fechaSalida:
      this.vuelo.fechaSalida,

      horaSalida:
      this.vuelo.horaSalida,

      fechaLlegada:
      this.vuelo.fechaLlegada,

      horaLlegada:
      this.vuelo.horaLlegada
    }];

    const worksheet =
      XLSX.utils.json_to_sheet(datos);

    const workbook = {
      Sheets: {
        'Vuelo': worksheet
      },
      SheetNames: ['Vuelo']
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
      'consulta-vuelo.xlsx'
    );

    alert(
      'Excel generado correctamente'
    );
  }

}
