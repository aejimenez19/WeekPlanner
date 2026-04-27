import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  imports: [RouterLink, FormsModule],
  templateUrl: './register.html'
})
export class RegisterPage {
  private authService = inject(AuthService);
  private router = inject(Router);

  email = signal('');
  password = signal('');
  fullName = signal('');
  showPassword = signal(false);

  isLoading = signal(false);
  error = signal<string | null>(null);

  togglePassword() {
    this.showPassword.update(v => !v);
  }

  onSubmit() {
    this.error.set(null);
    this.isLoading.set(true);

    this.authService.register({
      email: this.email(),
      password: this.password(),
      fullName: this.fullName()
    }).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.isLoading.set(false);
        this.error.set(err.error?.message || 'Error al registrar usuario');
      }
    });
  }
}
