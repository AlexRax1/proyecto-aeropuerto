import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  console.log('🔥 EL INTERCEPTOR SE ESTÁ EJECUTANDO PARA:', req.url);
  
  // Inyectamos el servicio directamente en la función
  const authService = inject(AuthService);
  const token = authService.obtenerToken();
  
  console.log('🔑 Token encontrado:', token ? 'Sí hay token' : 'NULO/VACÍO');

  // Si existe el token, clonamos la petición y agregamos el header
  if (token) {
    const requestClonada = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(requestClonada);
  }

  // Si no hay token, la petición sigue su curso normal
  return next(req);
};