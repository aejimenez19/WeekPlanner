import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { TaskService, Task } from '../../services/task.service';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule],
  templateUrl: './dashboard.html'
})
export class DashboardPage implements OnInit {
  private authService = inject(AuthService);
  private taskService = inject(TaskService);
  private router = inject(Router);

  isLoading = signal(false);
  error = signal<string | null>(null);

  ngOnInit() {
    this.loadTasks();
  }

  get tasks(): Task[] {
    return this.taskService.tasks();
  }

  getPendingTasks(): Task[] {
    return this.taskService.getPendingTasks();
  }

  getCompletedTasks(): Task[] {
    return this.taskService.getCompletedTasks();
  }

  get completionRate(): number {
    const all = this.taskService.tasks();
    if (all.length === 0) return 0;
    const completed = all.filter(t => t.completed).length;
    return Math.round((completed / all.length) * 100);
  }

  getPendingCount(): number {
    return this.getPendingTasks().length;
  }

  loadTasks() {
    this.isLoading.set(true);
    this.taskService.getTasks().subscribe({
      next: () => this.isLoading.set(false),
      error: (err) => {
        this.isLoading.set(false);
        this.error.set(err.error?.message || 'Error loading tasks');
      }
    });
  }

  toggleComplete(taskId: number) {
    this.taskService.toggleComplete(taskId).subscribe({
      error: (err) => {
        this.error.set(err.error?.message || 'Error updating task');
      }
    });
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}