import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'productos',
    pathMatch: 'full'
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/login').then((m) => m.LoginComponent)
  },
  {
    path: 'productos',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/productos/productos.component').then(
        (m) => m.ProductosComponent
      )
  },
  {
    path: 'movimientos',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/movimientos/movimientos.component').then(
        (m) => m.MovimientosComponent
      )
  },
  {
    path: '**',
    redirectTo: 'productos'
  }
];