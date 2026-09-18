import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Movimiento } from '../models/movimiento';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MovimientoService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/api/movimientos`;

  listar(): Observable<Movimiento[]> {
    return this.http.get<Movimiento[]>(this.apiUrl);
  }

  registrar(movimiento: { productoId: number, tipo: string, cantidad: number }): Observable<Movimiento> {
    return this.http.post<Movimiento>(this.apiUrl, movimiento);
  }
}