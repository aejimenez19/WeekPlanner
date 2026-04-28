import { Component, ChangeDetectionStrategy, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TaskService, TaskCreateRequest } from '../../services/task.service';

export type DayOfWeek = 'LUNES' | 'MARTES' | 'MIÉRCOLES' | 'JUEVES' | 'VIERNES' | 'SÁBADO' | 'DOMINGO';

@Component({
  selector: 'app-task-modal',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, FormsModule],
  templateUrl: './task-modal.html'
})
export class TaskModalComponent {
  private taskService = inject(TaskService);

  isVisible = signal(false);
  isSubmitting = signal(false);
  error = signal<string | null>(null);

  title = signal('');
  dayOfWeek = signal<DayOfWeek | ''>('');
  description = signal('');

  daysOfWeek: DayOfWeek[] = ['LUNES', 'MARTES', 'MIÉRCOLES', 'JUEVES', 'VIERNES', 'SÁBADO', 'DOMINGO'];

  isFormValid = computed(() => {
    return this.title().trim().length > 0 && this.dayOfWeek() !== '';
  });

  show() {
    this.isVisible.set(true);
    this.error.set(null);
  }

  hide() {
    this.isVisible.set(false);
    this.resetForm();
  }

  resetForm() {
    this.title.set('');
    this.dayOfWeek.set('');
    this.description.set('');
    this.error.set(null);
  }

  onBackdropClick(event: MouseEvent) {
    if ((event.target as HTMLElement).classList.contains('glass-overlay')) {
      this.hide();
    }
  }

  onKeydown(event: KeyboardEvent) {
    if (event.key === 'Escape') {
      this.hide();
    }
  }

  createTask() {
    if (!this.isFormValid() || this.isSubmitting()) return;

    this.isSubmitting.set(true);
    this.error.set(null);

    const taskData: TaskCreateRequest = {
      title: this.title().trim(),
      dayOfWeek: this.dayOfWeek() as DayOfWeek,
      description: this.description().trim() || undefined
    };

    this.taskService.createTask(taskData).subscribe({
      next: () => {
        this.isSubmitting.set(false);
        this.hide();
      },
      error: (err) => {
        this.isSubmitting.set(false);
        this.error.set(err.error?.message || 'Error creating task');
      }
    });
  }
}