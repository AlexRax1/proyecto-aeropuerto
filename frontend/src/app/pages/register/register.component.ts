import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  registerData = {

    username: '',

    pasaporte: '',
    nombreCompleto: '',
    fechaNacimiento: '',
    nacionalidad: '',
    correo: '',

    codigoArea: '',
    telefono: '',

    extensionNumeroEmergencia: '',
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

    // VALIDAR CAMPOS OBLIGATORIOS

    if (
      !data.username ||
      !data.pasaporte ||
      !data.nombreCompleto ||
      !data.fechaNacimiento ||
      !data.nacionalidad ||
      !data.correo ||
      !data.codigoArea ||
      !data.telefono ||
      !data.extensionNumeroEmergencia ||
      !data.numeroEmergencia ||
      !data.direccion ||
      !data.password
    ) {

      alert('Debe ingresar los campos obligatorios');
      return;
    }

    // VALIDAR PASAPORTE

    if (data.pasaporte.length > 15) {

      alert('El pasaporte no puede exceder 15 caracteres');
      return;
    }

    // VALIDAR TELÉFONO

    const telefonoRegex = /^\d{8}$/;

    if (!telefonoRegex.test(data.telefono)) {

      alert('El teléfono debe contener exactamente 8 dígitos');
      return;
    }

    // VALIDAR NÚMERO DE EMERGENCIA

    if (!telefonoRegex.test(data.numeroEmergencia)) {

      alert('El número de emergencia debe contener exactamente 8 dígitos');
      return;
    }

    // VALIDAR PASSWORD

    const passwordRegex =
      /^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&.#])[A-Za-z\d@$!%*?&.#]{6,}$/;

    if (!passwordRegex.test(data.password)) {

      alert(
        'La contraseña debe incluir al menos una mayúscula, un número y un carácter especial'
      );

      return;
    }

    // CONFIRMAR

    const confirmar = confirm('¿Está seguro de continuar?');

    if (!confirmar) {

      alert('Se ha cancelado el registro satisfactoriamente');
      return;
    }

    // REGISTRAR USUARIO

    this.http.post(
      'http://localhost:8082/usuarios/register',
      this.registerData,
      {
        responseType: 'text'
      }
    )
      .subscribe({

        next: (response: any) => {

          console.log(
            'Usuario registrado',
            response
          );

          // MOSTRAR MENSAJE DEL BACKEND

          if (response.message) {

            alert(response.message);

          } else {

            alert(
              'Se ha creado con éxito el usuario.'
            );
          }

          this.router.navigate(['/login']);
        },

        error: (err) => {

          console.error(err);

          // SI EL BACKEND ENVÍA JSON

          if (err.error?.message) {

            alert(err.error.message);

          }
          // SI ENVÍA TEXTO PLANO

          else if (typeof err.error === 'string') {

            alert(err.error);

          }
          // ERROR GENERAL

          else {

            alert(
              'Error al registrar usuario'
            );
          }
        }
      });
  }
}
