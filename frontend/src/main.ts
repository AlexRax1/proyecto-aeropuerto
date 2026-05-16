import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter } from '@angular/router';
import { provideHttpClient , withInterceptors } from '@angular/common/http';

import { AppComponent } from './app/app';

import { HomeComponent } from './app/pages/home/home.component';
import { AvionSelectionComponent } from './app/pages/avion-selection/avion-selection.component';
import { SeatSelectionComponent } from './app/pages/seat-selection/seat-selection.component';
import { PaymentComponent } from './app/pages/payment/payment.component';
import { ConfirmationComponent } from './app/pages/confirmation/confirmation.component';
import { LoginComponent } from './app/pages/login/login.component';
import { RegisterComponent } from './app/pages/register/register.component';
import { ConsultaVuelosComponent } from './app/pages/consulta_listVuelos/consulta-vuelos.component';
import { ConsultaAerolineasComponent } from './app/pages/consulta-aerolineas/consulta-aerolineas.component';
import { ConsultaAvionesComponent } from './app/pages/consulta-aviones/consulta-aviones.component';
import { ConsultaPasajerosVueloComponent } from './app/pages/consulta-pasajeros-vuelo/consulta-pasajeros-vuelo.component';
import {ConsultaDestinosComponent} from './app/pages/consulta-destinos/consulta-destinos.component';
import {ConsultaEquipajeComponent} from './app/pages/consulta-equipaje/consulta-equipaje.component';
import {AbordajeComponent} from './app/pages/abordaje/abordaje.component';
import {ReservarVueloComponent} from './app/pages/reservar-vuelo/reservar-vuelo.component';
import {ConsultaAgregarVueloComponent} from './app/pages/consulta-agregar-vuelo/consulta-agregar-vuelo.component';
import {CrearTripulacionComponent} from './app/pages/crear-tripulacion/crear-tripulacion.component';


import { authInterceptor } from './app/core/interceptors/auth.interceptor'; 

// 1. AGREGA 'withIntercept

bootstrapApplication(AppComponent, {
  providers: [
    
    // 3. MODIFICA ESTA LÍNEA PARA ENLAZAR EL INTERCEPTOR
    provideHttpClient(
      withInterceptors([authInterceptor])
    ),

    // TUS RUTAS SE QUEDAN EXACTAMENTE IGUAL
    provideRouter([
      { path: '', component: HomeComponent },
      { path: 'aviones', component: AvionSelectionComponent },
      { path: 'asientos', component: SeatSelectionComponent },
      { path: 'pago', component: PaymentComponent },
      { path: 'confirmacion', component: ConfirmationComponent },
      { path: 'login', component: LoginComponent },
      { path: 'registro', component: RegisterComponent },
      { path: 'consulta-vuelos', component: ConsultaVuelosComponent },
      { path: 'consulta-aerolineas', component: ConsultaAerolineasComponent },
      { path: 'consulta-aviones',  component: ConsultaAvionesComponent },
      { path: 'consulta-pasajeros-vuelo',  component: ConsultaPasajerosVueloComponent },
      { path: 'consulta-destinos',  component: ConsultaDestinosComponent },
      { path: 'consulta-equipaje',  component: ConsultaEquipajeComponent },
      { path: 'abordaje',  component: AbordajeComponent },
      { path: 'reservar-vuelo', component: ReservarVueloComponent },
      { path: 'consulta-agregar-vuelo',  component: ConsultaAgregarVueloComponent },
      { path: 'crear-tripulacion',  component: CrearTripulacionComponent },
      { path: '**', redirectTo: '' }
    ])
  ]
}).catch(err => console.error(err));