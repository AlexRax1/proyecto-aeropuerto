import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-consulta-equipaje',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './consulta-equipaje.component.html',
  styleUrls: ['./consulta-equipaje.component.css']
})
export class ConsultaEquipajeComponent {

  numeroVuelo: string = '';

  consultaRealizada = false;

  equipajes: any[] = [];

  buscarEquipaje() {

    if (!this.numeroVuelo) {

      alert('Debe ingresar el número de vuelo');

      return;
    }

    // FA03
    if (this.numeroVuelo === '0000') {

      alert('El número de vuelo ingresado no existe.');

      this.equipajes = [];

      this.consultaRealizada = false;

      return;
    }

    // FA04
    if (this.numeroVuelo === '9999') {

      alert('No puede consultar vuelos de otra aerolínea.');

      this.equipajes = [];

      this.consultaRealizada = false;

      return;
    }

    this.consultaRealizada = true;

    // Datos simulados
    this.equipajes = [
      {
        pasajero: 'Juan Pérez',
        maleta: 'Maleta Negra',
        peso: '23 kg'
      },
      {
        pasajero: 'María López',
        maleta: 'Maleta Azul',
        peso: '18 kg'
      },
      {
        pasajero: 'Carlos Ramírez',
        maleta: 'Maleta Roja',
        peso: '25 kg'
      }
    ];
  }

  limpiarFiltros() {

    this.numeroVuelo = '';

    this.equipajes = [];

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
