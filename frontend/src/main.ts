import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';

import { AppComponent } from './app/app';

import { HomeComponent } from './app/pages/home/home.component';
import { AvionSelectionComponent } from './app/pages/avion-selection/avion-selection.component';
import { SeatSelectionComponent } from './app/pages/seat-selection/seat-selection.component';
import { PaymentComponent } from './app/pages/payment/payment.component';
import { ConfirmationComponent } from './app/pages/confirmation/confirmation.component';
import { LoginComponent } from './app/pages/login/login.component';
import { RegisterComponent } from './app/pages/register/register.component';

bootstrapApplication(AppComponent, {
  providers: [
    provideHttpClient(),
    provideRouter([
      { path: '', component: HomeComponent },

      { path: 'aviones', component: AvionSelectionComponent },

      { path: 'asientos', component: SeatSelectionComponent },

      { path: 'pago', component: PaymentComponent },

      { path: 'confirmacion', component: ConfirmationComponent },

      { path: 'login', component: LoginComponent },

      { path: 'registro', component: RegisterComponent },

      { path: '**', redirectTo: '' }
    ])
  ]
}).catch(err => console.error(err));
