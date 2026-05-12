import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { forkJoin } from 'rxjs';

interface Seat {
  label: string;
  selected: boolean;
  occupied: boolean;

  categoria?: string;
  tipo?: string;
  estado?: string;
  fila?: string;
  columna?: string;
  idAsiento?: number;
}

@Component({
  selector: 'app-seat-selection',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './seat-selection.component.html',
  styleUrls: ['./seat-selection.component.css']
})
export class SeatSelectionComponent implements OnInit {

  seats: Seat[][] = [];
  maxSelection = 5;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.loadSeats(1, 1);
  }

  generateSeats() {
    this.seats = [];

    const rows = 30;
    const cols = ['A','B','C','D','E','F'];

    for (let i = 1; i <= rows; i++) {
      const row: Seat[] = [];

      for (let col of cols) {
        row.push({
          label: `${i}${col}`,
          selected: false,
          occupied: false
        });
      }

      this.seats.push(row);
    }
  }

  mockOccupiedSeats() {
    const data = ["1A", "2B", "3C", "5D"];

    this.seats.forEach(row => {
      row.forEach(seat => {
        if (data.includes(seat.label)) {
          seat.occupied = true;
        }
      });
    });
  }

  getSelectedCount(): number {
    return this.seats.flat().filter(s => s.selected).length;
  }

  selectedSeatInfo: Seat[] = [];

  selectSeat(seat: Seat) {
    if (seat.occupied) return;

    const selectedCount = this.getSelectedCount();

    if (!seat.selected && selectedCount >= this.maxSelection) {
      alert(`Solo puedes seleccionar ${this.maxSelection} asientos`);
      return;
    }

    seat.selected = !seat.selected;

    if (seat.selected) {
      this.selectedSeatInfo.push(seat);
    } else {
      this.selectedSeatInfo = this.selectedSeatInfo.filter(s => s !== seat);
    }
  }

  confirmSelection() {
    const selectedSeatsObjects = this.selectedSeatInfo;

    if (selectedSeatsObjects.length === 0) {
      alert('Debes seleccionar al menos un asiento');
      return;
    }
    

    // para pruebas, cambiar luego
    const idUsuario = 1;
    const idVueloActual = 1;





    selectedSeatsObjects.forEach(seat => {
      
      const payload = {
        vueloId: idVueloActual,
        usuarioId: idUsuario,
        asientoId: seat.idAsiento, // Enviamos el ID numérico
        codigoAsiento: seat.label,
        cantMaletas: 1, // Puedes quemar valores para la prueba
        costoBoleto: 150.00
      };

      this.http.post('http://localhost:8084/api/reservas/crear', payload)
        .subscribe({
          next: (res) => {
            console.log(`Asiento ${seat.label} reservado con éxito en la BD.`);
            // Si funciona, recargamos el mapa para que se pinte de rojo inmediatamente
            this.loadSeats(1, 1);
            this.selectedSeatInfo = []; // Limpiamos la selección
          },
          error: (err) => {
            console.error(`Error reservando el asiento ${seat.label}`, err);
            alert(`Ocurrió un error al reservar el asiento ${seat.label}`);
          }
        });
    });
  }

  loadSeats(avionId: number, vueloId: number) {
    const reqOperaciones = this.http.get<any>(`http://localhost:8083/aviones/${avionId}/asientos`);
    
    const reqReservas = this.http.get<number[]>(`http://localhost:8084/api/reservas/vuelo/${vueloId}/ocupados`);

    
    forkJoin({
      mapa:reqOperaciones,
      ocupados: reqReservas
    }).subscribe({
      next: (data) => {

          console.log("DATA BACKEND:", data.mapa);
          console.log("DATA BACKEND:", data.ocupados);

          this.seats = data.mapa.matrizAsientos.map((row: any[]) =>
            row.map(seat => {

            const estaOcupado = data.ocupados.includes(seat.idAsiento);
            return {
              label: `${seat.fila}${seat.columna}`,
              selected: false,
              occupied: estaOcupado,

              categoria: seat.categoria,
              tipo: seat.tipo,
              estado: estaOcupado ? 'OCUPADO': 'LIBRE',
              fila: seat.fila,
              columna: seat.columna,
              idAsiento: seat.idAsiento
            };
          })
        );
      },
      error: (err) => {
        console.error('Error cargando asientos', err);
      }
    });
  }
}
