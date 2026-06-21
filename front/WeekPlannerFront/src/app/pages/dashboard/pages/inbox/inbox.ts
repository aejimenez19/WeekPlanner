import { Component, inject, OnInit, signal, ViewChild, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TaskService, Task } from '../../../../services/task.service';
import { TaskModalComponent } from '../../../../components/task-modal/task-modal';
import { TaskCardComponent } from '../../../../components/task-card/task-card';
import { EmptyStateComponent } from '../../../../components/empty-state/empty-state';

@Component({
  selector: 'app-inbox',
  imports: [CommonModule, TaskModalComponent, TaskCardComponent, EmptyStateComponent],
  templateUrl: './inbox.html'
})
export class InboxPage implements OnInit {
  private taskService = inject(TaskService);
  
  @ViewChild('taskModalComponent') taskModal!: TaskModalComponent;

  isLoading = signal(false);
  error = signal<string | null>(null);

  pendingTasks = computed(() => this.taskService.tasks().filter(t => !t.completed));
  completedTasks = computed(() => this.taskService.tasks().filter(t => t.completed));
  pendingCount = computed(() => this.pendingTasks().length);
  completionRate = computed(() => {
    const all = this.taskService.tasks();
    if (all.length === 0) return 0;
    const completed = all.filter(t => t.completed).length;
    return Math.round((completed / all.length) * 100);
  });

  ngOnInit() {
    this.loadTasks();
  }
  
  openNewTaskModal() {
    this.taskModal.show();
  }

  get tasks(): Task[] {
    return this.taskService.tasks();
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

  toggleComplete(taskId: number, completed: boolean) {
    this.taskService.toggleComplete(taskId, completed).subscribe({
      error: (err) => {
        this.error.set(err.error?.message || 'Error updating task');
      }
    });
  }
}
