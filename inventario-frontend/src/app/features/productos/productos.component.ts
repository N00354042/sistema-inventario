import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductoService } from './services/producto.service';
import { ReporteService } from './services/reporte.service';
import { Producto } from './models/producto';
import { StockChartComponent } from './components/stock-chart.component';
import { AuthService } from '../auth/services/auth.service';

interface ProductoFormState {
  id?: number;
  nombre: string;
  descripcion: string;
  precio: number | null;
  stock: number | null;
  stockMinimo: number | null;
}

const FORM_INICIAL: ProductoFormState = {
  nombre: '',
  descripcion: '',
  precio: null,
  stock: null,
  stockMinimo: null
};

@Component({
  selector: 'app-productos',
  standalone: true,
  imports: [CommonModule, FormsModule, StockChartComponent],
  templateUrl: './productos.component.html'
})
export class ProductosComponent implements OnInit {
  private readonly productoService = inject(ProductoService);
  private readonly reporteService = inject(ReporteService);
  private readonly authService = inject(AuthService);

  esAdmin = this.authService.esAdmin;
  productos = signal<Producto[]>([]);
  cargando = signal<boolean>(false);
  error = signal<string | null>(null);

  terminoBusqueda = signal<string>('');
  filtroStock = signal<'TODOS' | 'BAJO' | 'NORMAL'>('TODOS');

  paginaActual = signal<number>(1);
  elementosPorPagina = signal<number>(5);

  productosFiltrados = computed(() => {
    const term = this.terminoBusqueda().toLowerCase().trim();
    const estado = this.filtroStock();

    return this.productos().filter((p) => {
      const coincideTexto =
        p.nombre.toLowerCase().includes(term) ||
        p.descripcion.toLowerCase().includes(term);

      if (!coincideTexto) return false;

      if (estado === 'BAJO') return p.stock <= p.stockMinimo;
      if (estado === 'NORMAL') return p.stock > p.stockMinimo;
      return true;
    });
  });

  totalPaginas = computed(() => {
    const total = this.productosFiltrados().length;
    const porPagina = this.elementosPorPagina();
    return Math.max(1, Math.ceil(total / porPagina));
  });

  productosPaginados = computed(() => {
    const items = this.productosFiltrados();
    const pagina = this.paginaActual();
    const porPagina = this.elementosPorPagina();

    const inicio = (pagina - 1) * porPagina;
    const fin = inicio + porPagina;
    return items.slice(inicio, fin);
  });

  modalAbierto = signal<boolean>(false);
  guardando = signal<boolean>(false);
  errorFormulario = signal<string | null>(null);
  modoEdicion = signal<boolean>(false);
  formulario = signal<ProductoFormState>({ ...FORM_INICIAL });

  modalEliminarAbierto = signal<boolean>(false);
  eliminando = signal<boolean>(false);
  productoAEliminar = signal<Producto | null>(null);

  ngOnInit(): void {
    this.cargarProductos();
  }

  cargarProductos(): void {
    this.cargando.set(true);
    this.error.set(null);

    this.productoService.listar().subscribe({
      next: (data) => {
        this.productos.set(data);
        this.cargando.set(false);
      },
      error: (err) => {
        this.error.set('Ocurrió un error al cargar el inventario.');
        this.cargando.set(false);
        console.error(err);
      }
    });
  }

  actualizarBusqueda(valor: string): void {
    this.terminoBusqueda.set(valor);
    this.paginaActual.set(1);
  }

  actualizarFiltroStock(valor: 'TODOS' | 'BAJO' | 'NORMAL'): void {
    this.filtroStock.set(valor);
    this.paginaActual.set(1);
  }

  cambiarElementosPorPagina(valor: number): void {
    this.elementosPorPagina.set(Number(valor));
    this.paginaActual.set(1);
  }

  cambiarPagina(nuevaPagina: number): void {
    if (nuevaPagina >= 1 && nuevaPagina <= this.totalPaginas()) {
      this.paginaActual.set(nuevaPagina);
    }
  }

  descargarExcel(): void {
    this.reporteService.exportarExcel(this.productosFiltrados());
  }

  descargarPdf(): void {
    this.reporteService.exportarPdf(this.productosFiltrados());
  }

  abrirModalCrear(): void {
    if (!this.esAdmin()) return;
    this.modoEdicion.set(false);
    this.errorFormulario.set(null);
    this.formulario.set({ ...FORM_INICIAL });
    this.modalAbierto.set(true);
  }

  abrirModalEditar(producto: Producto): void {
    if (!this.esAdmin()) return;
    this.modoEdicion.set(true);
    this.errorFormulario.set(null);
    this.formulario.set({
      id: producto.id,
      nombre: producto.nombre,
      descripcion: producto.descripcion,
      precio: producto.precio,
      stock: producto.stock,
      stockMinimo: producto.stockMinimo
    });
    this.modalAbierto.set(true);
  }

  cerrarModal(): void {
    if (this.guardando()) return;
    this.modalAbierto.set(false);
    this.formulario.set({ ...FORM_INICIAL });
    this.errorFormulario.set(null);
  }

  actualizarCampo<K extends keyof ProductoFormState>(campo: K, valor: ProductoFormState[K]): void {
    this.formulario.update((prev) => ({ ...prev, [campo]: valor }));
  }

  guardarProducto(): void {
    if (!this.esAdmin()) {
      this.errorFormulario.set('Acción denegada: Requiere rol ADMIN.');
      return;
    }

    const data = this.formulario();

    if (!data.nombre.trim()) {
      this.errorFormulario.set('El nombre es obligatorio.');
      return;
    }
    if (!data.descripcion.trim()) {
      this.errorFormulario.set('La descripción es obligatoria.');
      return;
    }
    if (data.precio === null || data.precio < 0) {
      this.errorFormulario.set('El precio debe ser un número mayor o igual a 0.');
      return;
    }
    if (data.stock === null || data.stock < 0) {
      this.errorFormulario.set('El stock debe ser un entero mayor o igual a 0.');
      return;
    }
    if (data.stockMinimo === null || data.stockMinimo < 0) {
      this.errorFormulario.set('El stock mínimo debe ser un entero mayor o igual a 0.');
      return;
    }

    const payload: Producto = {
      id: data.id,
      nombre: data.nombre.trim(),
      descripcion: data.descripcion.trim(),
      precio: Number(data.precio),
      stock: Math.floor(Number(data.stock)),
      stockMinimo: Math.floor(Number(data.stockMinimo))
    };

    this.guardando.set(true);
    this.errorFormulario.set(null);

    const peticion$ = this.modoEdicion()
      ? this.productoService.actualizar(payload)
      : this.productoService.guardar(payload);

    peticion$.subscribe({
      next: () => {
        this.guardando.set(false);
        this.cerrarModal();
        this.cargarProductos();
      },
      error: (err) => {
        this.guardando.set(false);
        const mensajeBackend = err.error?.message || 'Error al guardar el producto.';
        this.errorFormulario.set(mensajeBackend);
        console.error(err);
      }
    });
  }

  abrirModalEliminar(producto: Producto): void {
    if (!this.esAdmin()) return;
    this.productoAEliminar.set(producto);
    this.modalEliminarAbierto.set(true);
  }

  cerrarModalEliminar(): void {
    if (this.eliminando()) return;
    this.modalEliminarAbierto.set(false);
    this.productoAEliminar.set(null);
  }

  confirmarEliminar(): void {
    if (!this.esAdmin()) return;

    const prod = this.productoAEliminar();
    if (!prod || prod.id === undefined) return;

    this.eliminando.set(true);

    this.productoService.eliminar(prod.id).subscribe({
      next: () => {
        this.eliminando.set(false);
        this.cerrarModalEliminar();
        this.cargarProductos();
      },
      error: (err) => {
        this.eliminando.set(false);
        console.error(err);
        alert('No se pudo eliminar el producto.');
      }
    });
  }
}