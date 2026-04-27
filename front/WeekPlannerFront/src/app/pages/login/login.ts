import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  imports: [RouterLink, FormsModule],
  templateUrl: './login.html'
})
export class LoginPage {
  private authService = inject(AuthService);
  private router = inject(Router);

  email = signal('');
  password = signal('');

  isLoading = signal(false);
  error = signal<string | null>(null);

  onSubmit() {
    this.error.set(null);
    this.isLoading.set(true);

    this.authService.login({
      email: this.email(),
      password: this.password()
    }).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.isLoading.set(false);
        this.error.set(err.error?.message || 'Error al iniciar sesión');
      }
    });
  }
}