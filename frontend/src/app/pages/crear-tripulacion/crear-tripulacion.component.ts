import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-crear-tripulacion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './crear-tripulacion.component.html',
  styleUrls: ['./crear-tripulacion.component.css']
})
export class CrearTripulacionComponent {

  pilotos = [
    'Carlos Méndez',
    'Juan Pérez',
    'Luis García'
  ];

  copilotos = [
    'Mario López',
    'Andrés Soto',
    'Kevin Ruiz'
  ];

  ingenieros = [
    'José Ramírez',
    'Fernando Díaz',
    'Ricardo Morales'
  ];

  tripulantesCabina = [
    'Ana Torres',
    'María Gómez',
    'Sofía Morales',
    'Laura Castillo',
    'Daniela Flores',
    'Paula Hernández'
  ];

  tripulacion = {
    piloto: '',
    copiloto: '',
    ingeniero: '',
    cabina: [] as string[]
  };

  constructor(
    private router: Router
  ) {}

  toggleCabina(nombre: string) {

    const index =
      this.tripulacion.cabina.indexOf(nombre);

    if (index >= 0) {

      this.tripulacion.cabina.splice(index, 1);

    } else {

      if (this.tripulacion.cabina.length >= 3) {

        alert(
          'Solo puede seleccionar 3 tripulantes de cabina'
        );

        return;
      }

      this.tripulacion.cabina.push(nombre);
    }
  }

  guardarTripulacion() {

    if (
      !this.tripulacion.piloto ||
      !this.tripulacion.copiloto ||
      !this.tripulacion.ingeniero ||
      this.tripulacion.cabina.length !== 3
    ) {

      alert(
        'Debe ingresar los campos obligatorios'
      );

      return;
    }

    console.log(
      'Tripulación creada:',
      this.tripulacion
    );

    alert(
      'Se creó con éxito la tripulación'
    );

    this.router.navigate(['/login']);
  }

  limpiarFormulario() {

    this.tripulacion = {
      piloto: '',
      copiloto: '',
      ingeniero: '',
      cabina: []
    };
  }
}
