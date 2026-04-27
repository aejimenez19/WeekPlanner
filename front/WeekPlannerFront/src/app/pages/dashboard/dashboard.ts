import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-dashboard',
  template: `
    <div class="min-h-screen bg-background p-6">
      <header class="flex justify-end mb-8">
        <button (click)="logout()" class="btn-logout">
          Cerrar sesión
        </button>
      </header>
      
      <main>
        <h1 class="text-display-md text-on-surface">Dashboard</h1>
        <p class="text-body-lg text-on-surface-variant mt-2">¡Bienvenido a WeekPlanner!</p>
      </main>
    </div>
  `,
  styles: `
    .btn-logout {
      background: transparent;
      color: #4f634f;
      font-family: 'Manrope', sans-serif;
      font-weight: 500;
      font-size: 0.875rem;
      padding: 0.5rem 1rem;
      border-radius: 9999px;
      border: 1px solid rgba(79, 99, 79, 0.2);
      cursor: pointer;
      transition: background-color 0.2s ease;
    }
    
    .btn-logout:hover {
      background: #f3f4f2;
    }
  `
})
export class DashboardPage {
  private authService = inject(AuthService);
  private router = inject(Router);

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}