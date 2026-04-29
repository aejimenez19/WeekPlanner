import { Component, ChangeDetectionStrategy, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TaskService, Task, TaskCreateRequest } from '../../services/task.service';

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

  editingTask = signal<Task | null>(null);
  completed = signal(false);

  title = signal('');
  executionDate = signal('');
  time = signal('');
  description = signal('');

  isFormValid = computed(() => {
    return this.title().trim().length > 0;
  });

  isEditMode = computed(() => this.editingTask() !== null);

  show(task?: Task) {
    this.isVisible.set(true);
    this.error.set(null);

    if (task) {
      this.editingTask.set(task);
      this.title.set(task.title);
      this.executionDate.set(task.executionDate || '');
      this.time.set(task.time || '');
      this.description.set(task.description || '');
      this.completed.set(task.completed);
    } else {
      this.resetForm();
    }
  }

  hide() {
    this.isVisible.set(false);
    this.resetForm();
  }

  resetForm() {
    this.editingTask.set(null);
    this.completed.set(false);
    this.title.set('');
    this.executionDate.set('');
    this.time.set('');
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

  saveTask() {
    if (!this.isFormValid() || this.isSubmitting()) return;

    this.isSubmitting.set(true);
    this.error.set(null);

    const taskData: TaskCreateRequest = {
      title: this.title().trim(),
      description: this.description().trim() || undefined,
      executionDate: this.executionDate() || undefined,
      time: this.time() || undefined
    };

    if (this.isEditMode()) {
      const taskId = this.editingTask()!.id;
      this.taskService.updateTask(taskId, {
        ...taskData,
        completed: this.completed()
      }).subscribe({
        next: () => {
          this.isSubmitting.set(false);
          this.hide();
        },
        error: (err) => {
          this.isSubmitting.set(false);
          this.error.set(err.error?.message || 'Error updating task');
        }
      });
    } else {
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
}