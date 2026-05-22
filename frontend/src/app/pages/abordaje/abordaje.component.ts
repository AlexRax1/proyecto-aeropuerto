import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core'; // <-- Importamos ChangeDetectorRef
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-abordaje',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './abordaje.component.html',
  styleUrls: ['./abordaje.component.css']
})
export class AbordajeComponent implements OnInit {

  private http = inject(HttpClient);
  private cdr = inject(ChangeDetectorRef); // <-- Lo inyectamos aquí

  vuelos: any[] = [];
  vueloSeleccionado: any = null;
  pasaporte: string = '';
  cantidadMaletas: number | null = null;
  pasajerosAbordados: any[] = [];


 // NUEVA ESTRUCTURA DE MALETAS
  maletasFisicas: {
    maleta: string;
    peso: number | null;
  }[] = [];

  ngOnInit() {
    this.cargarVuelos();
  }

  // Convertimos a async/await e indicamos que redibuje el HTML
  async cargarVuelos() {
    try {
      const data = await firstValueFrom(
        this.http.get<any[]>('http://localhost:8083/vuelos/pendientesAbordar')
      );

      console.log('Vuelos recibidos desde el backend:', data); // Verifícalo en F12
      this.vuelos = data;

      // Magia: Forzamos a Angular a actualizar el HTML inmediatamente
      this.cdr.detectChanges();

    } catch (err) {
      console.error('Error al cargar vuelos', err);
    }
  }

  seleccionarVuelo(vuelo: any) {
    this.vueloSeleccionado = vuelo;
    this.pasaporte = '';
    this.cantidadMaletas = null;
    this.maletasFisicas = [];
  }

   generarCamposMaletas() {

    this.maletasFisicas = [];

    if (this.cantidadMaletas && this.cantidadMaletas > 0) {

      for (let i = 0; i < this.cantidadMaletas; i++) {

        this.maletasFisicas.push({
          maleta: '',
          peso: null
        });

      }
    }
  }

  async buscarPasajero() {

    if (!this.pasaporte || this.cantidadMaletas === null) {

      alert('Debe ingresar los campos obligatorios');

      return;
    }

    // VALIDAR MALETAS
    const maletasInvalidas = this.maletasFisicas.some(
      m =>
        !m.maleta ||
        m.peso === null ||
        m.peso <= 0
    );

    if (maletasInvalidas) {

      alert('Debe ingresar nombre y peso válido para todas las maletas.');

      return;
    }

    try {

      const usuario: any = await firstValueFrom(
        this.http.get(
          `http://localhost:8082/usuarios/pasaporte/${this.pasaporte}`
        )
      );

      const idUsuario = usuario.userId;

      const nombreUsuario = usuario.nombreCompleto;

      const idVuelo =
        this.vueloSeleccionado.vueloId ||
        this.vueloSeleccionado.id;

      // REQUEST FINAL
      const requestAbordaje = {

        idusuario: idUsuario,

        idVuelo: idVuelo,

        numMaletas: this.cantidadMaletas,

        nombrePasajero: nombreUsuario,

        maletas: this.maletasFisicas

      };


      console.log('Request enviado:', requestAbordaje);

      const respuestaAbordaje = await firstValueFrom(
        this.http.put(
          'http://localhost:8084/api/reservas/abordar',
          requestAbordaje,
          { responseType: 'text' }
        )
      );

      alert(respuestaAbordaje);

      this.pasajerosAbordados.push({

        pasaporte: this.pasaporte,

        maletas: this.cantidadMaletas,

        estado: 'ABORDADO'

      });

      // LIMPIAR FORMULARIO
      this.pasaporte = '';

      this.cantidadMaletas = null;

      this.maletasFisicas = [];

      this.cdr.detectChanges();

    } catch (error: any) {

      if (error.status === 404) {

        alert(
          'Error: Boleto no encontrado, pasajero no registrado o pasaporte incorrecto'
        );

      } else if (error.status === 400) {

        alert(
          error.error ||
          'Error: Validaciones de abordaje fallidas'
        );

      } else {

        alert('Ocurrió un error de conexión con el servidor');

        console.error(error);

      }
    }
  }

  async finalizarAbordaje() {
    if (!this.vueloSeleccionado) return;

    try {
      const idVuelo = this.vueloSeleccionado.vueloId || this.vueloSeleccionado.id;

      const respuestaCancelarBoletos = await firstValueFrom(
        this.http.put(`http://localhost:8084/api/reservas/vuelo/${idVuelo}/finalizar`, {}, { responseType: 'text' })
      );


      const respuestaActualizarVuelo = await firstValueFrom(
        this.http.put(`http://localhost:8083/vuelos/${idVuelo}/estado-abordado`, {}, { responseType: 'text' })
      );

      // 4. Mostrar mensaje consolidado (FA06)
      alert(`Se completó el abordaje.\nDetalle: ${respuestaCancelarBoletos}\n${respuestaActualizarVuelo}`);

      // 5. Limpiar vista y refrescar los vuelos (el vuelo abordado ya no debería aparecer)
      this.vueloSeleccionado = null;
      this.pasajerosAbordados = [];
      this.cargarVuelos();

    } catch (error) {
      alert('Error al intentar finalizar el abordaje y cambiar el estado del vuelo');
      console.error(error);
    }
  }

  nuevoVuelo() {
    this.vueloSeleccionado = null;
    this.pasajerosAbordados = [];
    this.pasaporte = '';
    this.cantidadMaletas = null;
  }
}
