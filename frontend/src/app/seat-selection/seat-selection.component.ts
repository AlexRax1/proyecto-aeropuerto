import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

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
    this.loadSeats(1);
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
    const selectedSeats = this.seats
      .flat()
      .filter(seat => seat.selected)
      .map(seat => seat.label);

    if (selectedSeats.length === 0) {
      alert('Debes seleccionar al menos un asiento');
      return;
    }

    console.log("Asientos seleccionados:", selectedSeats);

    alert(`Asientos reservados: ${selectedSeats.join(', ')}`);
  }

  loadSeats(avionId: number) {
  this.http.get<any>(`http://localhost:8083/aviones/${avionId}/asientos`)
    .subscribe({
      next: (data) => {

        console.log("DATA BACKEND:", data);

        this.seats = data.matrizAsientos.map((row: any[]) =>
          row.map(seat => ({
            label: `${seat.fila}${seat.columna}`,
            selected: false,
            occupied: seat.estado !== 'LIBRE',

            categoria: seat.categoria,
            tipo: seat.tipo,
            estado: seat.estado,
            fila: seat.fila,
            columna: seat.columna,
            idAsiento: seat.idAsiento
          }))
        );

        console.log("MATRIZ FINAL:", this.seats);
      },
      error: (err) => {
        console.error('Error cargando asientos', err);
      }
    });
  }
}