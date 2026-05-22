import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-consulta-equipaje',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './consulta-equipaje.component.html',
  styleUrls: ['./consulta-equipaje.component.css']
})
export class ConsultaEquipajeComponent {

  private http = inject(HttpClient);

  numeroVuelo: string = '';

  consultaRealizada = false;

  equipajes: any[] = [];

  async buscarEquipaje() {

    if (!this.numeroVuelo) {

      alert('Debe ingresar el número de vuelo');

      return;
    }

    const vueloId = parseInt(this.numeroVuelo, 10);

    if (isNaN(vueloId)) {

      alert('Ingrese un número de vuelo válido');

      return;
    }

    try {

      const respuesta: any[] = await firstValueFrom(

        this.http.get<any[]>(
          `http://localhost:8084/equipajes/vuelo/${vueloId}`
        )

      );

      console.log('Equipajes encontrados:', respuesta);

      this.equipajes = respuesta;

      this.consultaRealizada = true;

      if (this.equipajes.length === 0) {

        alert('No se encontraron equipajes para este vuelo.');

      }

    } catch (error) {

      console.error('Error al consultar equipajes:', error);

      alert('Error al comunicarse con el servidor.');

      this.equipajes = [];

      this.consultaRealizada = false;

    }
  }

  limpiarFiltros() {

    this.numeroVuelo = '';

    this.equipajes = [];

    this.consultaRealizada = false;

  }

  nuevaConsulta() {

    this.limpiarFiltros();

  }

  imprimirPDF() {

    alert('Generando archivo PDF...');

  }

  exportarExcel() {

    alert('Generando archivo Excel...');

  }
}