import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { forkJoin, Observable } from 'rxjs';

@Component({
  selector: 'app-payment',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})

export class PaymentComponent implements OnInit {

  asientosSeleccionados: any[] = [];
  vueloId: number = 0;
  usuarioId: number = 0;

  constructor(private router: Router, private http: HttpClient) {
    // 1. ATRAMAPOS LOS DATOS QUE VIENEN DE LA PANTALLA ANTERIOR
    const navigation = this.router.getCurrentNavigation();
    if (navigation?.extras.state) {
      this.asientosSeleccionados = navigation.extras.state['asientosReservados'];
      this.vueloId = navigation.extras.state['vueloId'];
      this.usuarioId = navigation.extras.state['usuarioId'];
    }
  }

  ngOnInit() {
    // Si recargan la página de pago, los datos se pierden. Los regresamos al mapa.
    if (!this.asientosSeleccionados || this.asientosSeleccionados.length === 0) {
      alert("No hay asientos seleccionados. Volviendo al mapa...");
      this.router.navigate(['/']); // Cambia '/' por la ruta de tu mapa si es otra
    }
  }

  // 2. EJECUTAMOS EL PAGO Y GUARDAMOS EN POSTGRES
  procesarPago() {
    // Volvemos a tipar el arreglo como Observable
    const peticionesPago: Observable<any>[] = [];

    this.asientosSeleccionados.forEach(seat => {
      const payload = {
        vueloId: this.vueloId,
        usuarioId: this.usuarioId,
        asientoId: seat.idAsiento,
        codigoAsiento: seat.label, // El "1A", "2B", etc.
        cantMaletas: 1,
        costoBoleto: 150.00
      };

      // Apuntamos al endpoint que guarda en BD y limpia el candado de Redis
      peticionesPago.push(
        this.http.post('http://localhost:8080/api/reservas/crear', payload)
      );
    });

    // Ejecutamos todos los POST al mismo tiempo
    forkJoin(peticionesPago).subscribe({
      next: (res) => {
        alert("¡Pago exitoso! Tus asientos están confirmados.");
        // Después de pagar con éxito, lo puedes mandar a una pantalla de éxito o al inicio
        this.router.navigate(['/']); 
      },
      error: (err) => {
        console.error("Error al procesar el pago", err);
        alert("Hubo un error al procesar tu pago. Intenta nuevamente.");
      }
    });
  }
}