import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class VueloService {

  private apiUrl =
    'http://localhost:8083';

  constructor(
    private http: HttpClient
  ) {}

  // ============================================
  // AEROPUERTOS
  // ============================================

  obtenerAeropuertos():
    Observable<any[]> {

    return this.http.get<any[]>(
      `http://localhost:8083/api/operaciones/destinos/select`
    );
  }

  // ============================================
  // AVIONES ACTIVOS
  // ============================================

  obtenerAvionesDisponibles(

    aerolineaId: number,

    fechaSalida: string,

    horaSalida: string,

    fechaLlegada: string,

    horaLlegada: string

  ): Observable<any[]> {

    return this.http.get<any[]>((

        `${this.apiUrl}/aviones/disponibles`),

      {
        params: {

          aerolineaId,

          fechaSalida,

          horaSalida: horaSalida + ':00',

          fechaLlegada,

          horaLlegada: horaLlegada + ':00'
        }
      }
    );
  }

  // ============================================
  // TRIPULACIÓN
  // ============================================
  obtenerTripulacionDisponible(

    fechaSalida: string,
    horaSalida: string,
    fechaLlegada: string,
    horaLlegada: string

  ): Observable<any[]> {

    return this.http.get<any[]>(

      `${this.apiUrl}/tripulacion/disponible`,

      {
        params: {

          fechaSalida,

          horaSalida,

          fechaLlegada,

          horaLlegada
        }
      }
    );
  }

  // ============================================
  // CREAR VUELO
  // ============================================

  crearVuelo(
    payload: any
  ): Observable<any> {

    return this.http.post(
      `${this.apiUrl}/vuelos`,
      payload
    );
  }

  obtenerTripulacionPorRol(rol: string) {

    return this.http.get<any[]>(
      `http://localhost:8083/tripulacion/rol?rol=${rol}`
    );
  }
}
