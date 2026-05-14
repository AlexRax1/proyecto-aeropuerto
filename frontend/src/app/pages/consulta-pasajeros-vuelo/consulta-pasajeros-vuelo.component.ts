import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-consulta-pasajeros-vuelo',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './consulta-pasajeros-vuelo.component.html',
  styleUrls: ['./consulta-pasajeros-vuelo.component.css']
})
export class ConsultaPasajerosVueloComponent {

  numeroVuelo: string = '';

  consultaRealizada = false;

  pasajeros: any[] = [];

  buscarPasajeros() {

    if (!this.numeroVuelo) {

      alert('Debe ingresar el número de vuelo');
      return;
    }

    this.consultaRealizada = true;

    // SIMULACIÓN FRONTEND

    // Vuelo inexistente
    if (this.numeroVuelo === '0000') {

      this.pasajeros = [];

      alert('El número de vuelo ingresado no existe.');

      return;
    }

    // Aerolínea distinta
    if (this.numeroVuelo === '9999') {

      this.pasajeros = [];

      alert('No puede consultar vuelos de otra aerolínea.');

      return;
    }

    // Datos simulados
    this.pasajeros = [

      {
        nombrePasajero: 'Juan Pérez',
        numeroPasaporte: 'A12345678',
        nacionalidad: 'Guatemalteco',
        edad: 32,
        telefono: '55554444',
        correo: 'juan@gmail.com'
      },

      {
        nombrePasajero: 'María López',
        numeroPasaporte: 'B87654321',
        nacionalidad: 'Salvadoreña',
        edad: 28,
        telefono: '44443333',
        correo: 'maria@gmail.com'
      },

      {
        nombrePasajero: 'Carlos Méndez',
        numeroPasaporte: 'C98765432',
        nacionalidad: 'Costarricense',
        edad: 40,
        telefono: '33332222',
        correo: 'carlos@gmail.com'
      }

    ];

  }

  limpiarFiltros() {

    this.numeroVuelo = '';

    this.pasajeros = [];

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
