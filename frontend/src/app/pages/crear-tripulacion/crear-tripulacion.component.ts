import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {Router} from '@angular/router';
import {VueloService} from '../services/vuelo.service';


@Component({
  selector: 'app-crear-tripulacion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './crear-tripulacion.component.html',
  styleUrls: ['./crear-tripulacion.component.css']
})
export class CrearTripulacionComponent
  implements OnInit {

  pilotos: any[] = [];

  copilotos: any[] = [];

  ingenieros: any[] = [];

  tripulantesCabina: any[] = [];

  tripulacion = {

    piloto: '',

    copiloto: '',

    ingeniero: '',

    cabina: [] as number[]
  };

  constructor(
    private router: Router,
    private vueloService: VueloService
  ) {}

  // ============================================
  // INIT
  // ============================================

  ngOnInit(): void {

    this.cargarPilotos();

    this.cargarCopilotos();

    this.cargarIngenieros();

    this.cargarSobrecargos();
  }

  // ============================================
  // CARGAR PILOTOS
  // ============================================

  cargarPilotos() {

    this.vueloService
      .obtenerTripulacionPorRol('Piloto')
      .subscribe({

        next: (data) => {

          this.pilotos = data;
        },

        error: (err) => {

          console.error(err);

          alert(
            'Error cargando pilotos'
          );
        }
      });
  }

  // ============================================
  // CARGAR COPILOTOS
  // ============================================

  cargarCopilotos() {

    this.vueloService
      .obtenerTripulacionPorRol('Copiloto')
      .subscribe({

        next: (data) => {

          this.copilotos = data;
        },

        error: (err) => {

          console.error(err);

          alert(
            'Error cargando copilotos'
          );
        }
      });
  }

  // ============================================
  // CARGAR INGENIEROS
  // ============================================

  cargarIngenieros() {

    this.vueloService
      .obtenerTripulacionPorRol(
        'Ingeniero de Vuelo'
      )
      .subscribe({

        next: (data) => {

          this.ingenieros = data;
        },

        error: (err) => {

          console.error(err);

          alert(
            'Error cargando ingenieros'
          );
        }
      });
  }

  // ============================================
  // CARGAR SOBRECARGOS
  // ============================================

  cargarSobrecargos() {

    this.vueloService
      .obtenerTripulacionPorRol(
        'Sobrecargo'
      )
      .subscribe({

        next: (data) => {

          this.tripulantesCabina = data;
        },

        error: (err) => {

          console.error(err);

          alert(
            'Error cargando sobrecargos'
          );
        }
      });
  }

  // ============================================
  // TOGGLE CABINA
  // ============================================

  toggleCabina(id: number) {

    const index =
      this.tripulacion.cabina.indexOf(id);

    if (index >= 0) {

      this.tripulacion.cabina.splice(index, 1);

    } else {

      if (
        this.tripulacion.cabina.length >= 3
      ) {

        alert(
          'Solo puede seleccionar 3 tripulantes de cabina'
        );

        return;
      }

      this.tripulacion.cabina.push(id);
    }
  }

  // ============================================
  // GUARDAR
  // ============================================

  guardarTripulacion() {

    if (
      !this.tripulacion.piloto ||
      !this.tripulacion.copiloto ||
      !this.tripulacion.ingeniero ||
      this.tripulacion.cabina.length !== 3
    ) {

      alert(
        'Debe ingresar los campos obligatorios'
      );

      return;
    }

    const payload = {

      pilotoId:
      this.tripulacion.piloto,

      copilotoId:
      this.tripulacion.copiloto,

      ingenieroId:
      this.tripulacion.ingeniero,

      sobrecargos:
      this.tripulacion.cabina,

      usuario:
        'admin'
    };

    console.log(
      'Tripulación creada:',
      payload
    );

    // ============================================
    // CUANDO TENGAS EL ENDPOINT:
    // this.vueloService.crearTripulacion(payload)
    // ============================================

    alert(
      'Se creó con éxito la tripulación'
    );

    this.router.navigate(['/login']);
  }

  // ============================================
  // LIMPIAR
  // ============================================

  limpiarFormulario() {

    this.tripulacion = {

      piloto: '',

      copiloto: '',

      ingeniero: '',

      cabina: []
    };
  }
}
