import { bootstrapApplication } from '@angular/platform-browser';
<<<<<<< HEAD
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { AppComponent } from './app/app';
import { SeatSelectionComponent } from './app/seat-selection/seat-selection.component';

bootstrapApplication(AppComponent, {
  providers: [
    provideHttpClient(),
    provideRouter([
      { path: '', redirectTo: 'asientos', pathMatch: 'full' },
      { path: 'asientos', component: SeatSelectionComponent },
      { path: '**', redirectTo: 'asientos' }
    ])
  ]
}).catch(err => console.error(err));
=======
import { appConfig } from './app/app.config';
import { App } from './app/app';

bootstrapApplication(App, appConfig)
  .catch((err) => console.error(err));
>>>>>>> 1e84a6313c7645cbdfa6e0d5f4f2b3539a6d4e8b
