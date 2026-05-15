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

  // 1. Recibimos el ID del vuelo dinámicamente desde el componente padre
  @Input() vueloId!: number;
  
  // para cerrar el modal y/o devolver los asientos seleccionados hacia el front
  @Output() cerrar = new EventEmitter<void>();
  @Output() asientosConfirmados = new EventEmitter<any[]>();

  seats: Seat[][] = [];
  maxSelection = 5;
  pollingSub!: Subscription;
  selectedSeatInfo: Seat[] = [];

  constructor(private http: HttpClient, private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    // Solo cargamos si realmente nos pasaron un vueloId
    if (this.vueloId) {
      this.loadSeats(this.vueloId);
      
      // Iniciar el polling silencioso cada 3 segundos
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

    // Aún en pruebas, luego lo cambiaremos por el ID del usuario logueado
    const idUsuario = 1; 

    const peticionesBloqueo: Observable<any>[] = [];

    selectedSeatsObjects.forEach(seat => {
      const payload = {
        vueloId: this.vueloId, // Usamos la variable de entrada
        usuarioId: idUsuario,
        asientoId: seat.idAsiento, 
        codigoAsiento: seat.label,
        cantMaletas: 1, //cambiar para luego 
        costoBoleto: 150.00
      };

      peticionesBloqueo.push(
        this.http.post('http://localhost:8080/api/reservas/iniciar-pago', payload, { responseType: 'text' })
      );
    });

    forkJoin(peticionesBloqueo).subscribe({
      next: (res) => {
        console.log("Asientos bloqueados en Redis con éxito.");
        
        this.asientosConfirmados.emit(selectedSeatsObjects);
        this.cerrar.emit(); 
      },
      error: (err) => {
        console.error("Error al bloquear", err);
        alert("Alguien más está intentando reservar o ya reservó estos asientos. Por favor, elige otros.");
        
        // Recargamos el mapa para mostrar los asientos que nos ganaron usando el vueloId
        this.loadSeats(this.vueloId);
        this.selectedSeatInfo = []; 
      }
    });
  }

  // 3. Ya no pedimos avionId, solo vueloId
  loadSeats(vueloId: number) {
    // 4. Apuntamos al nuevo endpoint de Operaciones (Puerto 8083)
    const reqOperaciones = this.http.get<any>(`http://localhost:8083/api/operaciones/vuelos/${vueloId}/asientos`);
    
    // Asumo que tu microservicio de reservas sigue corriendo en el 8080
    const reqReservas = this.http.get<any>(`http://localhost:8080/api/reservas/vuelo/${vueloId}/ocupados`);
    
    forkJoin({
      mapa: reqOperaciones,
      estado: reqReservas
    }).subscribe({
      next: (data) => {
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

  actualizarEstados(vueloId: number) {
    this.http.get<any>(`http://localhost:8080/api/reservas/vuelo/${vueloId}/ocupados`) 
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
  
  // 5. Botón opcional por si quieres cerrar el modal sin reservar
  cancelar() {
    this.cerrar.emit();
  }
}