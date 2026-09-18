import {
  Component,
  ElementRef,
  ViewChild,
  input,
  effect,
  OnDestroy
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { Chart, registerables } from 'chart.js';
import { Producto } from '../models/producto';

Chart.register(...registerables);

@Component({
  selector: 'app-stock-chart',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="bg-white dark:bg-gray-800 rounded-lg border border-gray-200 dark:border-gray-700 p-5 shadow-xs mb-6">
      <div class="flex items-center justify-between mb-4">
        <div>
          <h2 class="text-base font-bold text-gray-800 dark:text-gray-100">Comparativa de Stock</h2>
          <p class="text-xs text-gray-500 dark:text-gray-400">Niveles actuales frente al umbral de reposición mínima</p>
        </div>
        <span class="text-xs font-semibold px-2.5 py-1 rounded-md bg-blue-50 text-blue-700 dark:bg-blue-950 dark:text-blue-300">
          Total: {{ productos().length }} ítems
        </span>
      </div>

      <div class="relative w-full h-72">
        <canvas #chartCanvas></canvas>
      </div>
    </div>
  `
})
export class StockChartComponent implements OnDestroy {
  @ViewChild('chartCanvas') chartCanvas!: ElementRef<HTMLCanvasElement>;

  productos = input.required<Producto[]>();

  private chartInstance: Chart | null = null;

  constructor() {
    effect(() => {
      const items = this.productos();
      if (this.chartCanvas) {
        this.renderizarGrafica(items);
      }
    });
  }

  ngAfterViewInit(): void {
    this.renderizarGrafica(this.productos());
  }

  private renderizarGrafica(items: Producto[]): void {
    if (!this.chartCanvas?.nativeElement) return;

    if (this.chartInstance) {
      this.chartInstance.destroy();
      this.chartInstance = null;
    }

    if (items.length === 0) return;

    const ctx = this.chartCanvas.nativeElement.getContext('2d');
    if (!ctx) return;

    const labels = items.map((p) => p.nombre);
    const stockActual = items.map((p) => p.stock);
    const stockMinimo = items.map((p) => p.stockMinimo);

    this.chartInstance = new Chart(ctx, {
      type: 'bar',
      data: {
        labels,
        datasets: [
          {
            label: 'Stock Actual',
            data: stockActual,
            backgroundColor: 'rgba(37, 99, 235, 0.75)', 
            borderColor: 'rgb(37, 99, 235)',
            borderWidth: 1,
            borderRadius: 4
          },
          {
            label: 'Stock Mínimo',
            data: stockMinimo,
            backgroundColor: 'rgba(239, 68, 68, 0.75)',
            borderColor: 'rgb(239, 68, 68)',
            borderWidth: 1,
            borderRadius: 4
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'top',
            labels: {
              boxWidth: 12,
              font: { size: 12 }
            }
          },
          tooltip: {
            mode: 'index',
            intersect: false
          }
        },
        scales: {
          y: {
            beginAtZero: true,
            ticks: { precision: 0 }
          },
          x: {
            grid: { display: false }
          }
        }
      }
    });
  }

  ngOnDestroy(): void {
    if (this.chartInstance) {
      this.chartInstance.destroy();
    }
  }
}