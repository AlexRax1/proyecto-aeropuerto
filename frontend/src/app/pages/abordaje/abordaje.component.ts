import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-abordaje',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './abordaje.component.html',
  styleUrls: ['./abordaje.component.css']
})
export class AbordajeComponent {

  vuelos = [
    {
      numeroVuelo: 'AV101',
      origen: 'Guatemala',
      destino: 'Panamá',
      fechaHora: '2026-06-15 08:30',
      estado: 'PENDIENTE ABORDAR'
    },
    {
      numeroVuelo: 'CP205',
      origen: 'Costa Rica',
      destino: 'México',
      fechaHora: '2026-06-15 10:00',
      estado: 'PENDIENTE ABORDAR'
    }
  ];

  vueloSeleccionado: any = null;

  pasaporte: string = '';

  cantidadMaletas: number | null = null;

  pasajerosAbordados: any[] = [];

  seleccionarVuelo(vuelo: any) {

    this.vueloSeleccionado = vuelo;

    this.pasaporte = '';

    this.cantidadMaletas = null;
  }

  buscarPasajero() {

    // FA04
    if (!this.pasaporte || this.cantidadMaletas === null) {

      alert('Debe ingresar los campos obligatorios');

      return;
    }

    // FA05
    if (this.pasaporte === '0000') {

      alert('El pasajero no se encuentra registrado en el vuelo');

      return;
    }

    let recargo = 0;

    // FA07
    if (this.cantidadMaletas > 2) {

      const extras = this.cantidadMaletas - 2;

      recargo = extras * 50;

      alert(
        `Se agregó $${recargo} por recargo de equipaje`
      );
    }

    this.pasajerosAbordados.push({
      pasaporte: this.pasaporte,
      maletas: this.cantidadMaletas,
      estado: 'ABORDADO'
    });

    alert('Pasajero abordado correctamente');

    this.pasaporte = '';

    this.cantidadMaletas = null;
  }

  finalizarAbordaje() {

    // FA06
    alert('Se completó el abordaje');

    this.vueloSeleccionado = null;

    this.pasajerosAbordados = [];
  }

  nuevoVuelo() {

    // FA08
    this.vueloSeleccionado = null;

    this.pasajerosAbordados = [];

    this.pasaporte = '';

    this.cantidadMaletas = null;
  }
}
