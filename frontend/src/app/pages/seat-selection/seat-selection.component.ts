import { Component, OnInit, OnDestroy, ChangeDetectorRef, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { forkJoin, Observable, interval, Subscription } from 'rxjs';

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
export class SeatSelectionComponent implements OnInit, OnDestroy {

  @Input() vueloId!: number;
  @Output() cerrar = new EventEmitter<void>();
  @Output() asientosConfirmados = new EventEmitter<any[]>();

  seats: Seat[][] = [];
  maxSelection = 1;
  pollingSub!: Subscription;
  selectedSeatInfo: Seat[] = [];

  // Novedad: Guardamos qué letras preceden a un pasillo
  letrasConPasilloADerecha: string[] = [];

  constructor(private http: HttpClient, private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    if (this.vueloId) {
      this.loadSeats(this.vueloId);
      this.pollingSub = interval(3000).subscribe(() => {
        this.actualizarEstados(this.vueloId);
      });
    }
  }

  ngOnDestroy() {
    if (this.pollingSub) this.pollingSub.unsubscribe();
  }

  getSelectedCount(): number {
    return this.seats.flat().filter(s => s.selected).length;
  }

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

    const peticionesBloqueo: Observable<any>[] = [];

    selectedSeatsObjects.forEach(seat => {
      const payload = {
        vueloId: this.vueloId,
        asientoId: seat.idAsiento,
        codigoAsiento: seat.label,
        cantMaletas: 1,
        costoBoleto: seat.categoria === 'EJECUTIVA' ? 300.00 : 150.00 // Ejemplo básico
      };

      peticionesBloqueo.push(
        this.http.post('http://localhost:8080/api/reservas/iniciar-pago', payload, { responseType: 'text' })
      );
    });

    forkJoin(peticionesBloqueo).subscribe({
      next: (res) => {
        console.log("Asientos bloqueados con éxito.");
        this.asientosConfirmados.emit(selectedSeatsObjects);
        this.cerrar.emit();
      },
      error: (err) => {
        console.error("Error al bloquear", err);
        alert("Alguien más está intentando reservar. Por favor, elige otros.");
        this.loadSeats(this.vueloId);
        this.selectedSeatInfo = [];
      }
    });
  }

  loadSeats(vueloId: number) {
    const reqOperaciones = this.http.get<any>(`http://localhost:8080/vuelos/${vueloId}/asientos`);
    const reqReservas = this.http.get<any>(`http://localhost:8080/api/reservas/vuelo/${vueloId}/ocupados`);

    forkJoin({ mapa: reqOperaciones, estado: reqReservas }).subscribe({
      next: (data) => {
          // Lógica para detectar el pasillo basada en "ABC-DEF"
          this.letrasConPasilloADerecha = this.calcularPasillos(data.mapa.mapaColumnas);

          this.seats = data.mapa.matrizAsientos.map((row: any[]) =>
            row.map(seat => {
            const estaOcupado = data.estado.ocupados.includes(seat.idAsiento);
            const estaBloqueado = data.estado.bloqueados.includes(seat.idAsiento);

            return {
              label: `${seat.fila}${seat.columna}`,
              selected: false,
              occupied: estaOcupado || estaBloqueado,
              categoria: seat.categoria,
              tipo: seat.tipo,
              estado: estaOcupado ? 'OCUPADO' : (estaBloqueado ? 'BLOQUEADO' : 'LIBRE'),
              fila: seat.fila,
              columna: seat.columna,
              idAsiento: seat.idAsiento
            };
          })
        );
      },
      error: (err) => console.error('Error cargando asientos', err)
    });
  }

  // Novedad: Extrae la letra anterior a un guion
  // Si el mapa es "ABC-DEF", devolverá ["C"]
  // Si el mapa es "AB-CD-EF", devolverá ["B", "D"]
  calcularPasillos(mapaOriginal: string): string[] {
    const letrasPasillo = [];
    for (let i = 0; i < mapaOriginal.length; i++) {
      if (mapaOriginal[i] === '-' && i > 0) {
        letrasPasillo.push(mapaOriginal[i - 1]);
      }
    }
    return letrasPasillo;
  }

  actualizarEstados(vueloId: number) {
    this.http.get<any>(`http://localhost:8080/api/reservas/vuelo/${vueloId}/ocupados`).subscribe({
        next: (estado) => {
          this.seats.forEach(row => {
            row.forEach(seat => {
              if (estado.ocupados.includes(seat.idAsiento)) {
                seat.occupied = true;
                seat.estado = 'OCUPADO';
                seat.selected = false;
              } else if (estado.bloqueados.includes(seat.idAsiento)) {
                seat.occupied = true;
                seat.estado = 'BLOQUEADO';
                if (seat.selected) {
                  seat.selected = false;
                  this.selectedSeatInfo = this.selectedSeatInfo.filter(s => s.idAsiento !== seat.idAsiento);
                }
              } else {
                seat.occupied = false;
                seat.estado = 'LIBRE';
              }
            });
          });
          this.cdr.detectChanges();
        },
        error: (err) => console.error("Error en polling", err)
      });
  }
  tienePasilloADerecha(columna?: string): boolean {
    if (!columna) return false;
    return this.letrasConPasilloADerecha.includes(columna);
  }
  cancelar() {
    this.cerrar.emit();
  }
}
