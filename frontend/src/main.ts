import { bootstrapApplication } from '@angular/platform-browser';
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