import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const roleGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const expectedRoles = route.data['expectedRoles'] as string[];

  // Si no se requiere ningún rol específico, o no se configuró, dejar pasar
  if (!expectedRoles || expectedRoles.length === 0) {
    return true;
  }

  // Comprobar si el usuario tiene al menos uno de los roles requeridos
  const hasRole = expectedRoles.some(role => authService.hasRole(role));

  if (!hasRole) {
    // Si no tiene el rol, redirigir a alguna página (por ejemplo el home o una página de no autorizado)
    // Redirigimos dependiendo de si está logueado y qué roles tiene, o directamente a /
    if (!authService.isLoggedIn()) {
      router.navigate(['/login']);
    } else {
      router.navigate(['/']); // Redirigir a inicio o ruta de acceso denegado
    }
    return false;
  }

  return true;
};
