import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MovimientoService } from './services/movimiento.service';
import { Movimiento } from './models/movimiento';
import { ProductoService } from '../productos/services/producto.service';
import { Producto } from '../productos/models/producto';

@Component({
  selector: 'app-movimientos',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './movimientos.component.html'
})
export class MovimientosComponent implements OnInit {
  private readonly movimientoService = inject(MovimientoService);
  private readonly productoService = inject(ProductoService);
  private readonly fb = inject(FormBuilder);

  movimientos = signal<Movimiento[]>([]);
  productos = signal<Producto[]>([]);
  
  cargando = signal<boolean>(false);
  guardando = signal<boolean>(false);
  error = signal<string | null>(null);
  
  mensajeExito = signal<string | null>(null);
  mensajeErrorForm = signal<string | null>(null);

  movimientoForm: FormGroup = this.fb.group({
    productoId: ['', Validators.required],
    tipo: ['ENTRADA', Validators.required],
    cantidad: [1, [Validators.required, Validators.min(1)]]
  });

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.cargando.set(true);
    this.error.set(null);

    this.productoService.listar().subscribe({
      next: (data) => this.productos.set(data),
      error: () => this.error.set('Error al cargar productos.')
    });

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

  registrarMovimiento(): void {
    if (this.movimientoForm.invalid) return;

    this.guardando.set(true);
    this.mensajeExito.set(null);
    this.mensajeErrorForm.set(null);

    const formValue = {
      productoId: Number(this.movimientoForm.value.productoId),
      tipo: this.movimientoForm.value.tipo,
      cantidad: Number(this.movimientoForm.value.cantidad)
    };

    this.movimientoService.registrar(formValue).subscribe({
      next: () => {
        this.mensajeExito.set('Movimiento registrado exitosamente.');
        this.guardando.set(false);
        this.movimientoForm.reset({ tipo: 'ENTRADA', cantidad: 1 });
        this.cargarDatos(); 
      },
      error: (err) => {
        const errorMsg = err.error?.message || 'Error de validación al registrar el movimiento.';
        this.mensajeErrorForm.set(errorMsg);
        this.guardando.set(false);
      }
    });
  }
}