import { Injectable } from '@angular/core';
import * as XLSX from 'xlsx';
import { saveAs } from 'file-saver';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import { Producto } from '../models/producto';

@Injectable({
  providedIn: 'root'
})
export class ReporteService {

  exportarExcel(productos: Producto[]): void {
    if (!productos || productos.length === 0) {
      alert('No hay datos disponibles para exportar.');
      return;
    }

    const dataAplanada = productos.map((p) => ({
      ID: p.id ?? '-',
      Nombre: p.nombre,
      Descripción: p.descripcion,
      'Precio ($)': Number(p.precio).toFixed(2),
      Stock: p.stock,
      'Stock Mínimo': p.stockMinimo,
      Estado: p.stock <= p.stockMinimo ? 'STOCK BAJO' : 'NORMAL'
    }));

    const worksheet: XLSX.WorkSheet = XLSX.utils.json_to_sheet(dataAplanada);
    const workbook: XLSX.WorkBook = {
      Sheets: { Productos: worksheet },
      SheetNames: ['Productos']
    };

    const excelBuffer: ArrayBuffer = XLSX.write(workbook, {
      bookType: 'xlsx',
      type: 'array'
    });

    const blob = new Blob([excelBuffer], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8'
    });

    saveAs(blob, `reporte_productos_${this.obtenerFechaFormateada()}.xlsx`);
  }

  exportarPdf(productos: Producto[]): void {
    if (!productos || productos.length === 0) {
      alert('No hay datos disponibles para exportar.');
      return;
    }

    const doc = new jsPDF();

    doc.setFontSize(18);
    doc.text('Inventario Pro - Catálogo de Productos', 14, 20);
    
    doc.setFontSize(10);
    doc.text(`Fecha de emisión: ${new Date().toLocaleString()}`, 14, 28);

    const columnas = ['ID', 'Nombre', 'Descripción', 'Precio', 'Stock', 'Stock Mín.', 'Estado'];
    const filas = productos.map((p) => [
      p.id ?? '-',
      p.nombre,
      p.descripcion,
      `$${Number(p.precio).toFixed(2)}`,
      p.stock.toString(),
      p.stockMinimo.toString(),
      p.stock <= p.stockMinimo ? 'BAJO' : 'OK'
    ]);

    autoTable(doc, {
      head: [columnas],
      body: filas,
      startY: 34,
      theme: 'grid',
      headStyles: {
        fillColor: [37, 99, 235], 
        textColor: 255,
        fontStyle: 'bold'
      },
      styles: {
        fontSize: 9,
        cellPadding: 3
      },
      alternateRowStyles: {
        fillColor: [248, 250, 252] 
      }
    });

    doc.save(`reporte_productos_${this.obtenerFechaFormateada()}.pdf`);
  }

  private obtenerFechaFormateada(): string {
    const hoy = new Date();
    const yyyy = hoy.getFullYear();
    const mm = String(hoy.getMonth() + 1).padStart(2, '0');
    const dd = String(hoy.getDate()).padStart(2, '0');
    return `${yyyy}-${mm}-${dd}`;
  }
}