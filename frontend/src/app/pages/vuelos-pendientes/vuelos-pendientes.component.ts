import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-vuelos-pendientes',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './vuelos-pendientes.component.html',
  styleUrls: ['./vuelos-pendientes.component.css']
})
export class VuelosPendientesComponent implements OnInit {
  vuelos: any[] = [];
  loading = true;
  error = false;

  constructor(
    private http: HttpClient, 
    private router: Router, 
    public authService: AuthService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.cargarVuelos();
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
