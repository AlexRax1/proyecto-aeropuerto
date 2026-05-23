import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  loginData = {
    username: '',
    password: ''
  };

  constructor(
    private http: HttpClient,
    private router: Router,
    private authService: AuthService
  ) {}

  login() {

    if (!this.loginData.username || !this.loginData.password) {
      alert('Completa todos los campos');
      return;
    }

    this.http.post('http://localhost:8081/auth/login', this.loginData)
      .subscribe({
        next: (response: any) => {

          console.log('Login exitoso', response);

          // Guardamos el token en el local storage
          this.authService.guardarToken(response.token);

          alert('Bienvenido');

          // Verificamos el rol para redirigir
          if (this.authService.hasRole('ROLE_ADMIN')) {
            this.router.navigate(['/admin']);
          } else if (this.authService.hasRole('ROLE_USER')) {
            // El usuario normal entra a reservar vuelo (ClienteLayout)
            this.router.navigate(['/cliente']); 
          } else if (this.authService.hasRole('ROLE_MANAGER')) {
            // El manager (rol 3) entra al layout de "user" para los abordajes y tripulación
            this.router.navigate(['/user']);
          } else {
            // Ruta por defecto si tiene otro rol
            this.router.navigate(['/']);
          }
        },
        error: (err) => {
          console.error(err);
          alert('Usuario o contraseña incorrectos');
        }
      });
  }
}
