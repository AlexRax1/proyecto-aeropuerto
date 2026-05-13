import { Component, OnInit, OnDestroy, ChangeDetectorRef} from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { forkJoin, Observable, interval, Subscription} from 'rxjs';

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

  seats: Seat[][] = [];
  maxSelection = 5;

  pollingSub!: Subscription;

  constructor(private http: HttpClient, private router: Router, private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    this.loadSeats(1, 1);
    
    // Iniciar el polling silencioso cada 3 segundos usando el endpoint original
    this.pollingSub = interval(3000).subscribe(() => {
      this.actualizarEstados(1); 
    });
  }

  ngOnDestroy() {
    // Apagar el polling al salir de la pantalla
    if (this.pollingSub) this.pollingSub.unsubscribe();
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

    const peticionesBloqueo: Observable<any>[] = [];

    selectedSeatsObjects.forEach(seat => {
      
      const payload = {
        vueloId: idVueloActual,
        usuarioId: idUsuario,
        asientoId: seat.idAsiento, // Enviamos el ID numérico
        codigoAsiento: seat.label,
        cantMaletas: 1, //valores para la prueba
        costoBoleto: 150.00
      };

      
      
      peticionesBloqueo.push(
        this.http.post('http://localhost:8084/api/reservas/iniciar-pago', payload, { responseType: 'text' })
      );
    });

    // Ejecutamos todos los bloqueos al mismo tiempo
    forkJoin(peticionesBloqueo).subscribe({
      next: (res) => {
        console.log("Asientos bloqueados en Redis con éxito.");
        
        // NAVEGAMOS A LA PANTALLA DE PAGO ENVIANDO LOS DATOS
        this.router.navigate(['/pago'], {
          state: { 
            asientosReservados: selectedSeatsObjects, 
            vueloId: idVueloActual, 
            usuarioId: idUsuario 
          }
        });
      },
      error: (err) => {
        console.error("Error al bloquear", err);
        alert("Alguien más está intentando reservar o ya reservó estos asientos. Por favor, elige otros.");
        
        // Recargamos el mapa para mostrar los asientos que nos ganaron
        this.loadSeats(1, 1);
        this.selectedSeatInfo = []; 
      }
    });
  }

  loadSeats(avionId: number, vueloId: number) {
    const reqOperaciones = this.http.get<any>(`http://localhost:8083/aviones/${avionId}/asientos`);
    const reqReservas = this.http.get<any>(`http://localhost:8084/api/reservas/vuelo/${vueloId}/ocupados`);
    
    forkJoin({
      mapa: reqOperaciones,
      estado: reqReservas
    }).subscribe({
      next: (data) => {
          this.seats = data.mapa.matrizAsientos.map((row: any[]) =>
            row.map(seat => {

            // Leemos del nuevo formato JSON
            const estaOcupado = data.estado.ocupados.includes(seat.idAsiento);
            const estaBloqueado = data.estado.bloqueados.includes(seat.idAsiento);

            return {
              label: `${seat.fila}${seat.columna}`,
              selected: false,
              occupied: estaOcupado || estaBloqueado, // Se deshabilita si está en cualquiera de las dos listas
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

  actualizarEstados(vueloId: number) {
    this.http.get<any>(`http://localhost:8084/api/reservas/vuelo/${vueloId}/ocupados`) // Tu URL original
      .subscribe({
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
                
                // Si el usuario tenía este asiento en verde, se le quita
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

          // MAGIA: Obliga a Angular a redibujar los colores al instante
          this.cdr.detectChanges(); 
        },
        error: (err) => console.error("Error en polling", err)
      });
  }

  
}
