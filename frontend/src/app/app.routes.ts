import { Routes } from '@angular/router';

import { HomeComponent } from './pages/home/home.component';
import { AvionSelectionComponent } from './pages/avion-selection/avion-selection.component';
import { SeatSelectionComponent } from './pages/seat-selection/seat-selection.component';
import { PaymentComponent } from './pages/payment/payment.component';
import { ConfirmationComponent } from './pages/confirmation/confirmation.component';
import { ConsultaVuelosComponent } from './pages/consulta_listVuelos/consulta-vuelos.component';
import { ConsultaAerolineasComponent } from './pages/consulta-aerolineas/consulta-aerolineas.component';
import { ConsultaAvionesComponent } from './pages/consulta-aviones/consulta-aviones.component';
import { ConsultaPasajerosVueloComponent } from './pages/consulta-pasajeros-vuelo/consulta-pasajeros-vuelo.component';
import { ConsultaDestinosComponent } from './pages/consulta-destinos/consulta-destinos.component';
import {ConsultaEquipajeComponent} from './pages/consulta-equipaje/consulta-equipaje.component';
import {AbordajeComponent} from './pages/abordaje/abordaje.component';
import {ReservarVueloComponent} from './pages/reservar-vuelo/reservar-vuelo.component';
import {ConsultaAgregarVueloComponent} from './pages/consulta-agregar-vuelo/consulta-agregar-vuelo.component';
import {CrearTripulacionComponent} from './pages/crear-tripulacion/crear-tripulacion.component';
import {ConsultaVueloComponent} from './pages/consulta-vuelo/consulta-vuelo.component';
import {AdminLayoutComponent} from './shared/layout/admin-layout.component';
import {ClienteLayoutComponent} from './shared/layout/cliente-layout.component';
import {UserLayoutComponent} from './shared/layout/user-layout.component';
import {RegisterComponent} from './pages/register/register.component';
import {LoginComponent} from './pages/login/login.component';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';
import { VuelosPendientesComponent } from './pages/vuelos-pendientes/vuelos-pendientes.component';

export const routes: Routes = [

  // =====================================
  // PÚBLICAS
  // =====================================

  {
    path: '',
    redirectTo: 'cliente',
    pathMatch: 'full'
  },

  {
    path: 'login',
    component: LoginComponent
  },

  {
    path: 'registro',
    component: RegisterComponent
  },

  // =====================================
  // ADMIN
  // =====================================

  {
    path: 'admin',
    component: AdminLayoutComponent,
    canActivate: [authGuard, roleGuard],
    data: { expectedRoles: ['ROLE_ADMIN'] },
    children: [

      {
        path: '',
        redirectTo: 'consulta-vuelo',
        pathMatch: 'full'
      },

      {
        path: 'consulta-vuelo',
        component: ConsultaVueloComponent
      },

      {
        path: 'consulta-vuelos',
        component: ConsultaVuelosComponent
      },

      {
        path: 'consulta-aerolineas',
        component: ConsultaAerolineasComponent
      },

      {
        path: 'consulta-aviones',
        component: ConsultaAvionesComponent
      },

      {
        path: 'consulta-pasajeros-vuelo',
        component: ConsultaPasajerosVueloComponent
      },

      {
        path: 'consulta-destinos',
        component: ConsultaDestinosComponent
      },

      {
        path: 'consulta-equipaje',
        component: ConsultaEquipajeComponent
      }

    ]
  },

  // =====================================
  // USER
  // =====================================

  {
    path: 'user',
    component: UserLayoutComponent,
    canActivate: [authGuard, roleGuard],
    data: { expectedRoles: ['ROLE_MANAGER'] }, // Manager maneja abordajes, tripulación, y agregar-vuelo
    children: [

      {
        path: '',
        redirectTo: 'crear-vuelo',
        pathMatch: 'full'
      },

      {
        path: 'abordaje',
        component: AbordajeComponent
      },

      {
        path: 'crear-tripulacion',
        component: CrearTripulacionComponent
      },

      {
        path: 'crear-vuelo',
        component: ConsultaAgregarVueloComponent
      }

    ]
  },

  // =====================================
  // CLIENTE
  // =====================================

  {
    path: 'cliente',
    component: ClienteLayoutComponent,
    children: [

      // Removed default redirect to vuelos-pendientes as requested

      {
        path: 'vuelos-pendientes',
        component: VuelosPendientesComponent
      },

      {
        path: 'reservar-vuelo',
        component: ReservarVueloComponent,
        canActivate: [authGuard, roleGuard],
        data: { expectedRoles: ['ROLE_USER'] }
      }

    ]
  },

  // =====================================
  // REDIRECCIÓN
  // =====================================

  {
    path: '**',
    redirectTo: 'cliente'
  }

];

