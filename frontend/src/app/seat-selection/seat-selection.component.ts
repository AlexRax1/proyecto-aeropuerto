import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

interface Seat {
  label: string;
  selected: boolean;
  occupied: boolean;
}

@Component({
  selector: 'app-seat-selection',
  standalone: true,
  imports: [CommonModule], // 👈 IMPORTANTE
  templateUrl: './seat-selection.component.html',
  styleUrls: ['./seat-selection.component.css']
})
export class SeatSelectionComponent implements OnInit {

  seats: Seat[][] = [];

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.generateSeats();
    this.mockOccupiedSeats();
    //this.loadOccupiedSeats(); con back
  }

  // 🔹 Genera los asientos
  generateSeats() {
    const rows = 10;
    const cols = ['A','B','C','D'];

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

  // 🔹 Simula asientos ocupados
  mockOccupiedSeats() {
    const data = ["1A", "2B", "3C", "5D"];

    data.forEach((occupiedSeat: string) => {
      this.seats.forEach(row => {
        row.forEach(seat => {
          if (seat.label === occupiedSeat) {
            seat.occupied = true;
          }
        });
      });
    });
  }

  // 🔹 Seleccionar asiento
  selectSeat(seat: Seat) {
    if (seat.occupied) return;

    seat.selected = !seat.selected;
  }

  // 🔹 Simulación de reserva
  confirmSelection() {
    const selectedSeats = this.seats
      .flat()
      .filter(seat => seat.selected)
      .map(seat => seat.label);

    console.log("Asientos seleccionados:", selectedSeats);

    alert('Simulación: Asientos reservados');
  }

  // 🔹 (Para cuando tengas backend)
  loadOccupiedSeats() {
    this.http.get<any>('http://localhost:8083/asientos')
      .subscribe(data => {
        this.seats = data.matrizAsientos.map((row: any[]) =>
          row.map(seat => ({
            label: `${seat.fila}${seat.columna}`,
            selected: false,
            occupied: seat.estado !== 'LIBRE'
          }))
        );
      });
  }
}