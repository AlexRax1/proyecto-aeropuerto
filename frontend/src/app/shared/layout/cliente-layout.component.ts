import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

import {
  Router,
  RouterLink,
  RouterOutlet,
  NavigationEnd
} from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-cliente-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    RouterOutlet
  ],
  templateUrl: './cliente-layout.component.html',
  styleUrls: ['./cliente-layout.component.css']
})
export class ClienteLayoutComponent implements OnInit {

  vuelos: any[] = [];
  loading = true;
  error = false;
  isHome = true;

  constructor(
    public authService: AuthService,
    public router: Router,
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      this.isHome = event.url === '/cliente' || event.url === '/cliente/';
    });
  }

  ngOnInit() {
    this.isHome = this.router.url === '/cliente' || this.router.url === '/cliente/';
    this.cargarVuelos();
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/cliente']);
  }

  async cargarVuelos() {
    try {
      this.loading = true;
      const data = await firstValueFrom(
        this.http.get<any[]>('http://localhost:8080/vuelos/pendientesAbordar')
      );

      console.log('Vuelos recibidos desde el backend:', data);
      this.vuelos = data;
      this.loading = false;
      
      this.cdr.detectChanges();
    } catch (err) {
      console.error('Error al cargar vuelos', err);
      this.error = true;
      this.loading = false;
      this.cdr.detectChanges();
    }
  }

  getAirportCode(location: any): string {
    if (!location) return 'UNK';
    if (typeof location === 'string') return location.substring(0, 3).toUpperCase();
    if (location.codigoIata) return location.codigoIata;
    if (location.nombreAeropuerto) return location.nombreAeropuerto.substring(0, 3).toUpperCase();
    return 'UNK';
  }

  getCityName(location: any): string {
    if (!location) return 'Desconocido';
    if (typeof location === 'string') return location;
    if (location.ciudad) return location.ciudad;
    if (location.nombreAeropuerto) return location.nombreAeropuerto;
    return 'Desconocido';
  }
}
