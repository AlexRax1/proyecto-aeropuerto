import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  registerData = {
    pasaporte: '',
    nombreCompleto: '',
    fechaNacimiento: '',
    nacionalidad: '',
    correo: '',
    codigoArea: '',
    telefono: '',
    numeroEmergencia: '',
    direccion: '',
    password: ''
  };

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  register() {

    const data = this.registerData;

    // Validar campos obligatorios
    if (
      !data.pasaporte ||
      !data.nombreCompleto ||
      !data.fechaNacimiento ||
      !data.nacionalidad ||
      !data.correo ||
      !data.codigoArea ||
      !data.telefono ||
      !data.numeroEmergencia ||
      !data.direccion ||
      !data.password
    ) {
      alert('Debe ingresar los campos obligatorios');
      return;
    }

    // Validar pasaporte
    if (data.pasaporte.length > 15) {
      alert('El pasaporte no puede exceder 15 caracteres');
      return;
    }

    // Validar teléfono
    const telefonoRegex = /^\d{8}$/;

    if (!telefonoRegex.test(data.telefono)) {
      alert('El teléfono debe contener exactamente 8 dígitos');
      return;
    }

    // Validar contraseña
    const passwordRegex =
      /^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&.#])[A-Za-z\d@$!%*?&.#]{6,}$/;

    if (!passwordRegex.test(data.password)) {
      alert(
        'El formato de la contraseña debe incluir al menos una letra mayúscula, un carácter especial y un número'
      );
      return;
    }

    // Validar pasaporte existente
    this.http.get<any>(
      `http://localhost:8083/usuarios/pasaporte/${data.pasaporte}`
    )
      .subscribe({

        next: (response) => {

          // Si ya existe usuario
          if (response.existe) {
            alert('El número de pasaporte ingresado ya cuenta con usuario.');
            return;
          }

          // Confirmación
          const confirmar = confirm('¿Está seguro de continuar?');

          if (!confirmar) {
            alert('Se ha cancelado el registro satisfactoriamente');
            return;
          }

          // Registrar usuario
          this.http.post(
            'http://localhost:8083/auth/register',
            this.registerData
          )
            .subscribe({

              next: (response) => {

                console.log('Usuario registrado', response);

                alert('Se ha creado con éxito el usuario.');

                this.router.navigate(['/login']);
              },

              error: (err) => {
                console.error(err);
                alert('Error al registrar usuario');
              }

            });

        },

        error: (err) => {
          console.error(err);
          alert('Error validando pasaporte');
        }

      });

  }
}
