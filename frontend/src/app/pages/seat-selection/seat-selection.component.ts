import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

interface Seat {
  idAsiento: number;
  label: string;
  selected: boolean;
  occupied: boolean;
  categoria: string;
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
  
  // Variables para info adicional del avión
  configAvion: any = null;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.loadOccupiedSeats();
  }

  loadOccupiedSeats() {
    // URL de tu microservicio de operaciones/aviones
    const endpoint = 'http://localhost:8083/aviones/1/asientos';

    this.http.get<any>(endpoint).subscribe({
      next: (data) => {
        this.configAvion = data; // Guardamos la info general (idAvion, aerolinea, etc.)
        
        // Mapeamos la matriz que viene del backend
        this.seats = data.matrizAsientos.map((row: any[]) =>
          row.map(seat => ({
            idAsiento: seat.idAsiento,
            label: `${seat.fila}${seat.columna}`,
            selected: false,
            occupied: seat.estado !== 'LIBRE',
            categoria: seat.categoria
          }))
        );
      },
      error: (err) => {
        console.error('Error cargando asientos:', err);
        // Aquí podrías llamar a un método de emergencia que genere 
        // una matriz vacía si el backend falla.
      }
    });
  }

  selectSeat(seat: Seat) {
    if (seat.occupied) return;

    const selectedCount = this.seats.flat().filter(s => s.selected).length;

    if (!seat.selected && selectedCount >= this.maxSelection) {
      alert(`Solo puedes seleccionar hasta ${this.maxSelection} asientos`);
      return;
    }

    seat.selected = !seat.selected;
  }

  confirmSelection() {
    const selectedSeats = this.seats
      .flat()
      .filter(seat => seat.selected);

    if (selectedSeats.length === 0) {
      alert('Debes seleccionar al menos un asiento');
      return;
    }

    // Aquí enviarías los IDs al microservicio de Reservas
    console.log("IDs a reservar:", selectedSeats.map(s => s.idAsiento));
    alert(`Reservando: ${selectedSeats.map(s => s.label).join(', ')}`);
  }
}