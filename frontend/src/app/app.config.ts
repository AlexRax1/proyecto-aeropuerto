import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';

// Importamos withInterceptors en lugar de withInterceptorsFromDi
import { provideHttpClient, withInterceptors } from '@angular/common/http';

// Asegúrate de importar la nueva CONSTANTE (la función), no la clase antigua
import { authInterceptor } from './core/interceptors/auth.interceptor'; // Ajusta tu ruta si es necesario

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    // Registramos el interceptor funcional aquí directamente
    provideHttpClient(
      withInterceptors([authInterceptor])
    )
  ]
};