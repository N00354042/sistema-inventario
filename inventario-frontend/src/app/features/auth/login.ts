import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html'
})


export class LoginComponent {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  username = signal<string>('');
  password = signal<string>('');
  mensaje = signal<string>('');
  cargando = signal<boolean>(false);

  login(): void {
    this.mensaje.set('');
    this.cargando.set(true);
    localStorage.removeItem('token'); 

    this.authService.login({ 
      username: this.username(), 
      password: this.password() 
    }).subscribe({
      next: (response) => {
        localStorage.setItem('token', response.token);
        this.cargando.set(false);
        this.router.navigate(['/productos']); 
      },
      error: () => {
        this.mensaje.set('Credenciales incorrectas o error de conexión');
        this.cargando.set(false);
      }
    });
  }
}