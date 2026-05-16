import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';

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
    private router: Router
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

          localStorage.setItem('token', response.token);

          alert('Bienvenido');

          this.router.navigate(['/']);
        },
        error: (err) => {
          console.error(err);
          alert('Usuario o contraseña incorrectos');
        }
      });
  }
}
