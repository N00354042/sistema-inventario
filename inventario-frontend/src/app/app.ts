import { Component, inject, signal, OnInit } from '@angular/core';
import { RouterOutlet, RouterLink, Router, NavigationEnd } from '@angular/router';
import { CommonModule } from '@angular/common';
import { filter } from 'rxjs';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink],
  templateUrl: './app.html'
})
export class AppComponent implements OnInit {
  private readonly router = inject(Router);

  darkMode = signal<boolean>(false);
  estaLogueado = signal<boolean>(false);

  ngOnInit(): void {
    const modoGuardado = localStorage.getItem('darkMode');
    if (modoGuardado) {
      const isDark = JSON.parse(modoGuardado);
      this.darkMode.set(isDark);
      if (isDark) {
        document.body.classList.add('dark');
      }
    }

    this.verificarSesion();

    this.router.events
      .pipe(filter((event): event is NavigationEnd => event instanceof NavigationEnd))
      .subscribe(() => {
        this.verificarSesion();
      });
  }

  private verificarSesion(): void {
    this.estaLogueado.set(!!localStorage.getItem('token'));
  }

  toggleDarkMode(): void {
    const current = !this.darkMode();
    this.darkMode.set(current);
    localStorage.setItem('darkMode', JSON.stringify(current));
    
    if (current) {
      document.body.classList.add('dark');
    } else {
      document.body.classList.remove('dark');
    }
  }

  logout(): void {
    localStorage.removeItem('token');
    this.verificarSesion();
    this.router.navigate(['/login']);
  }
}