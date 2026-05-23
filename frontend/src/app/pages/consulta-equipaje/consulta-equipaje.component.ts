import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import * as XLSX from 'xlsx';
import * as FileSaver from 'file-saver';

@Component({
  selector: 'app-consulta-equipaje',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './consulta-equipaje.component.html',
  styleUrls: ['./consulta-equipaje.component.css']
})
export class ConsultaEquipajeComponent {

  private http = inject(HttpClient);

  numeroVuelo: string = '';

  consultaRealizada = false;

  equipajes: any[] = [];

  async buscarEquipaje() {

    if (!this.numeroVuelo) {

      alert('Debe ingresar el número de vuelo');

      return;
    }

    const vueloId = parseInt(this.numeroVuelo, 10);

    if (isNaN(vueloId)) {

      alert('Ingrese un número de vuelo válido');

      return;
    }

    try {

      const respuesta: any[] = await firstValueFrom(

        this.http.get<any[]>(
          `http://localhost:8084/equipajes/vuelo/${vueloId}`
        )

      );

      console.log('Equipajes encontrados:', respuesta);

      this.equipajes = respuesta;

      this.consultaRealizada = true;

      if (this.equipajes.length === 0) {

        alert('No se encontraron equipajes para este vuelo.');

      }

    } catch (error) {

      console.error('Error al consultar equipajes:', error);

      alert('Error al comunicarse con el servidor.');

      this.equipajes = [];

      this.consultaRealizada = false;

    }
  }

  limpiarFiltros() {

    this.numeroVuelo = '';

    this.equipajes = [];

    this.consultaRealizada = false;

  }

  nuevaConsulta() {

    this.limpiarFiltros();

  }

  // PDF
  imprimirPDF() {

    if (this.equipajes.length === 0) {

      alert(
        'No hay equipajes para imprimir'
      );

      return;
    }

    const doc = new jsPDF();

    doc.text(
      'Consulta de Equipajes',
      14,
      15
    );

    autoTable(doc, {

      startY: 25,

      head: [[
        'Pasajero',
        'Maleta',
        'Peso'
      ]],

      body: this.equipajes.map(e => [

        e.pasajero,
        e.maleta,
        e.peso

      ])

    });

    doc.save(
      'consulta-equipaje.pdf'
    );

    alert(
      'PDF generado correctamente'
    );
  }

  // EXCEL
  exportarExcel() {

    if (this.equipajes.length === 0) {

      alert(
        'No hay equipajes para exportar'
      );

      return;
    }

    const datos = this.equipajes.map(e => ({

      pasajero: e.pasajero,

      maleta: e.maleta,

      peso: e.peso

    }));

    const worksheet =
      XLSX.utils.json_to_sheet(
        datos
      );

    const workbook = {

      Sheets: {
        'Equipajes': worksheet
      },

      SheetNames: ['Equipajes']
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
      'consulta-equipaje.xlsx'
    );

    alert(
      'Excel generado correctamente'
    );
  }
}
