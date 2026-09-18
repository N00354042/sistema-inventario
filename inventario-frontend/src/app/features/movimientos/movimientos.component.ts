import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MovimientoService } from './services/movimiento.service';
import { Movimiento } from './models/movimiento';

@Component({
  selector: 'app-movimientos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './movimientos.component.html'
})


export class MovimientosComponent implements OnInit {
  private readonly movimientoService = inject(MovimientoService);

  movimientos = signal<Movimiento[]>([]);
  cargando = signal<boolean>(false);
  error = signal<string | null>(null);

  ngOnInit(): void {
    this.cargarMovimientos();
  }

  cargarMovimientos(): void {
    this.cargando.set(true);
    this.error.set(null);

    this.movimientoService.listar().subscribe({
      next: (data) => {
        this.movimientos.set(data);
        this.cargando.set(false);
      },
      error: (err) => {
        this.error.set('Ocurrió un error al cargar el historial de movimientos.');
        this.cargando.set(false);
        console.error(err);
      }
    });
  }
}