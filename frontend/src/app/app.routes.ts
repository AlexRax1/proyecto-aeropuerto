import { Routes } from '@angular/router';

import { HomeComponent } from './pages/home/home.component';
import { AvionSelectionComponent } from './pages/avion-selection/avion-selection.component';
import { SeatSelectionComponent } from './pages/seat-selection/seat-selection.component';
import { PaymentComponent } from './pages/payment/payment.component';
import { ConfirmationComponent } from './pages/confirmation/confirmation.component';

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent
  },
  {
    path: 'aviones',
    component: AvionSelectionComponent
  },
  {
    path: 'asientos',
    component: SeatSelectionComponent
  },
  {
    path: 'pago',
    component: PaymentComponent
  },
  {
    path: 'confirmacion',
    component: ConfirmationComponent
  }
];
