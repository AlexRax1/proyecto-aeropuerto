import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
<<<<<<< HEAD
=======

>>>>>>> 1e84a6313c7645cbdfa6e0d5f4f2b3539a6d4e8b
import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
<<<<<<< HEAD
    provideRouter(routes),
=======
    provideRouter(routes)
>>>>>>> 1e84a6313c7645cbdfa6e0d5f4f2b3539a6d4e8b
  ]
};
