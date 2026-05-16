import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  guardarToken(token: string) {
    localStorage.setItem('token', token);
  }

  obtenerToken(): string | null {
    return localStorage.getItem('token');
  }

  obtenerDatosUsuario(): any | null {
    const token = this.obtenerToken();
    if (!token) return null;

    try {
      const payloadBase64Url = token.split('.')[1];
      const base64 = payloadBase64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));

      return JSON.parse(jsonPayload); 
    } catch (error) {
      console.error('Error al decodificar el JWT', error);
      return null;
    }
  }

  obtenerUsername(): string {
    const datos = this.obtenerDatosUsuario();
    return datos ? datos.sub : ''; 
  }
}