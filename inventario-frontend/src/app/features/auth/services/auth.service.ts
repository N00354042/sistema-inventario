import { Injectable, inject, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { jwtDecode } from 'jwt-decode';
import { AuthRequest, AuthResponse } from '../models/auth.model';
import { environment } from '../../../environments/environment';

interface JwtPayloadCustom {
  sub: string;
  rol: string;
  exp: number; 
}

@Injectable({
  providedIn: 'root'
})

export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/auth`;

  token = signal<string | null>(localStorage.getItem('token'));

  private readonly tokenPayload = computed<JwtPayloadCustom | null>(() => {
    const rawToken = this.token();
    if (!rawToken) return null;

    try {
      return jwtDecode<JwtPayloadCustom>(rawToken);
    } catch {
      return null;
    }
  });

  estaAutenticado = computed<boolean>(() => {
    const payload = this.tokenPayload();
    if (!payload?.exp) return false;

    return payload.exp * 1000 > Date.now();
  });

  rol = computed<string | null>(() => {
    if (!this.estaAutenticado()) return null;
    return this.tokenPayload()?.rol || null;
  });

  esAdmin = computed<boolean>(() => {
    if (!this.estaAutenticado()) return false;
    const r = this.rol();
    if (!r) return false;
    return r.toUpperCase() === 'ADMIN' || r.toUpperCase() === 'ROLE_ADMIN';
  });

  login(credentials: AuthRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap((res) => {
        if (res.token) {
          localStorage.setItem('token', res.token);
          this.token.set(res.token);
        }
      })
    );
  }

  logout(): void {
    localStorage.removeItem('token');
    this.token.set(null);
  }
}