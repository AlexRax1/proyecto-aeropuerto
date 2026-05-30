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
  selector: 'app-consulta-pasajeros-vuelo',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    HttpClientModule
  ],
  templateUrl: './consulta-pasajeros-vuelo.component.html',
  styleUrls: ['./consulta-pasajeros-vuelo.component.css']
})
export class ConsultaPasajerosVueloComponent {

  constructor(
    private http: HttpClient
  ) {}

  numeroVuelo: string = '';

  consultaRealizada = false;

  pasajeros: any[] = [];

  apiUrl =
    'http://localhost:8080/vuelos/pasajeros';

  // =========================================
  // BUSCAR
  // =========================================

  buscarPasajeros() {

    if (!this.numeroVuelo) {

      alert(
        'Debe ingresar el número de vuelo'
      );

      return;
    }

    this.http.get<any[]>(
      `${this.apiUrl}/${this.numeroVuelo}`
    ).subscribe({

      next: (response) => {

        console.log(response);

        this.pasajeros = response;

        this.consultaRealizada = true;

        if (this.pasajeros.length === 0) {

          alert(
            'No existen pasajeros para este vuelo'
          );

        } else {

          alert(
            'Consulta realizada correctamente'
          );
        }
      },

      error: (error) => {

        console.error(error);

        this.pasajeros = [];

        this.consultaRealizada = false;

        alert(
          'El número de vuelo ingresado no existe.'
        );
      }

    });
  }

  // =========================================
  // LIMPIAR
  // =========================================

  limpiarFiltros() {

    this.numeroVuelo = '';

    this.pasajeros = [];

    this.consultaRealizada = false;

    alert(
      'Consulta limpiada correctamente'
    );
  }

  // =========================================
  // NUEVA CONSULTA
  // =========================================

  nuevaConsulta() {

    this.limpiarFiltros();

    alert(
      'Nueva consulta iniciada'
    );
  }

  // =========================================
  // PDF
  // =========================================

  imprimirPDF() {

    if (this.pasajeros.length === 0) {

      alert(
        'No hay pasajeros para imprimir'
      );

      return;
    }

    const doc = new jsPDF();

    doc.text(
      'Consulta Pasajeros por Vuelo',
      14,
      15
    );

    autoTable(doc, {

      startY: 25,

      head: [[
        'Nombre',
        'Pasaporte',
        'Nacionalidad',
        'Edad',
        'Teléfono',
        'Correo'
      ]],

      body: this.pasajeros.map(p => [

        p.nombrePasajero,
        p.numeroPasaporte,
        p.nacionalidad,
        p.edad,
        p.telefono,
        p.correo

      ])

    });

    doc.save(
      'consulta-pasajeros.pdf'
    );

    alert(
      'PDF generado correctamente'
    );
  }

  // =========================================
  // EXCEL
  // =========================================

  exportarExcel() {

    if (this.pasajeros.length === 0) {

      alert(
        'No hay pasajeros para exportar'
      );

      return;
    }

    const worksheet =
      XLSX.utils.json_to_sheet(
        this.pasajeros
      );

    const workbook = {

      Sheets: {
        'Pasajeros': worksheet
      },

      SheetNames: ['Pasajeros']
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
      'consulta-pasajeros.xlsx'
    );

    alert(
      'Excel generado correctamente'
    );
  }

}
