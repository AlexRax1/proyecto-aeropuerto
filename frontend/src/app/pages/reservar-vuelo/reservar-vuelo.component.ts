import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';

// 1. Definimos las interfaces basadas en los DTOs de Spring Boot
export interface Destino {
  id: number;
  labelCompleto: string;
}

export interface Vuelo {
  numeroVuelo: number;
  modelo: string;
  origen: string;
  destino: string;
  salida: string;
  llegada: string;
  tiempo: string;
  economica: string;
  ejecutiva: string;
}

@Component({
  selector: 'app-reservar-vuelo',
  standalone: true,
  // 2. Importamos HttpClientModule para poder hacer peticiones
  imports: [CommonModule, FormsModule, HttpClientModule], 
  templateUrl: './reservar-vuelo.component.html',
  styleUrls: ['./reservar-vuelo.component.css']
})
export class ReservarVueloComponent implements OnInit {

  // 3. Inicializamos las variables con las interfaces
  aeropuertos: Destino[] = [];
  vuelosDisponibles: Vuelo[] = [];
  
  filtros = {
    origen: '', 
    destino: '',
    fechaSalida: ''
  };

  vueloSeleccionado: any = null;
  claseSeleccionada: string = '';
  asientoSeleccionado: string = '';
  cantidadMaletas: number | null = null;
  consultaRealizada = false;

  // 4. Inyectamos el HttpClient en el constructor
  constructor(private http: HttpClient) {}

  // 5. Cargar los aeropuertos al iniciar el componente
  ngOnInit(): void {
    this.http.get<Destino[]>('http://localhost:8083/api/operaciones/destinos/select')
      .subscribe({
        next: (data) => {
          this.aeropuertos = data;
        },
        error: (err) => {
          console.error('Error al cargar destinos:', err);
          alert('Error de conexión con el servidor al cargar aeropuertos.');
        }
      });
  }

  buscarVuelos() {
    // FA02: Validación de campos vacíos
    if (!this.filtros.origen || !this.filtros.destino || !this.filtros.fechaSalida) {
      alert('Debe ingresar los campos obligatorios');
      return;
    }

    // FA03: Origen y destino iguales
    if (this.filtros.origen === this.filtros.destino) {
      alert('No se puede seleccionar el mismo aeropuerto de salida y llegada.');
      return;
    }

    this.consultaRealizada = true;

    // 6. Construir la URL con los parámetros
    const url = `http://localhost:8083/api/operaciones/vuelos/buscar?origenId=${this.filtros.origen}&destinoId=${this.filtros.destino}&fechaSalida=${this.filtros.fechaSalida}`;

    // 7. Consumir el endpoint de vuelos
    this.http.get<Vuelo[]>(url).subscribe({
      next: (data) => {
        this.vuelosDisponibles = data;
        
        // FA04: Si el array viene vacío
        if (this.vuelosDisponibles.length === 0) {
          alert('No se encontraron vuelos según los parámetros ingresados');
        }
      },
      error: (err) => {
        console.error('Error al buscar vuelos:', err);
        alert('Ocurrió un error al consultar los vuelos disponibles.');
      }
    });
  }

  seleccionarVuelo(vuelo: Vuelo) {
    this.vueloSeleccionado = vuelo;
  }

  verDetalle(vuelo: Vuelo) {
    alert(
      `Modelo: ${vuelo.modelo}\nNúmero de vuelo: ${vuelo.numeroVuelo}\nOrigen: ${vuelo.origen}\nDestino: ${vuelo.destino}\nSalida: ${vuelo.salida}\nLlegada: ${vuelo.llegada}`
    );
  }

  generarPase() {
    if (!this.claseSeleccionada || !this.asientoSeleccionado || this.cantidadMaletas === null) {
      alert('Debe ingresar los campos obligatorios');
      return;
    }

    if (this.asientoSeleccionado === 'A1') {
      alert('No se puede seleccionar el asiento porque ya está ocupado');
      return;
    }

    alert('Pase de abordar generado correctamente');
  }

  nuevaBusqueda() {
    this.filtros = { origen: '', destino: '', fechaSalida: '' };
    this.vuelosDisponibles = [];
    this.vueloSeleccionado = null;
    this.claseSeleccionada = '';
    this.asientoSeleccionado = '';
    this.cantidadMaletas = null;
    this.consultaRealizada = false;
  }
}