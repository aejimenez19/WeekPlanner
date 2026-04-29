import { Component, inject, OnInit, signal, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TaskService, Task } from '../../../../services/task.service';
import { TaskModalComponent } from '../../../../components/task-modal/task-modal';

@Component({
  selector: 'app-plan',
  imports: [CommonModule, FormsModule, TaskModalComponent],
  templateUrl: './plan.html'
})
export class PlanPage implements OnInit {
  private taskService = inject(TaskService);
  
  @ViewChild('taskModalComponent') taskModal!: TaskModalComponent;

  isLoading = signal(false);
  error = signal<string | null>(null);
  searchQuery = signal('');

  readonly daysEnglish = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
  weekDates: Date[] = [];


  private getWeekDates(): Date[] {
    const today = new Date();
    const currentDay = today.getDay();
    const monday = new Date(today);
    monday.setDate(today.getDate() - (currentDay === 0 ? 6 : currentDay - 1));
    
    return Array.from({ length: 7 }, (_, i) => {
      const date = new Date(monday);
      date.setDate(monday.getDate() + i);
      return date;
    });
  }

  private dateToStr(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  formatDateDisplay(date: Date): string {
    return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
  }

  ngOnInit() {
    this.weekDates = this.getWeekDates();
    this.loadTasks();
  }
  
  openNewTaskModal() {
    this.taskModal.show();
  }

  private tasksByDate = new Map<string, { pending: Task[]; completed: Task[] }>();

  private refreshTasksByDate() {
    this.tasksByDate.clear();
    for (const task of this.taskService.tasks()) {
      if (!task.executionDate) continue;
      const dateKey = String(task.executionDate);
      if (!this.tasksByDate.has(dateKey)) {
        this.tasksByDate.set(dateKey, { pending: [], completed: [] });
      }
      const entry = this.tasksByDate.get(dateKey)!;
      if (task.completed) {
        entry.completed.push(task);
      } else {
        entry.pending.push(task);
      }
    }
  }

  getUnscheduledTasks(): Task[] {
    return this.taskService.tasks().filter(t => 
      !t.executionDate && !t.time && !t.completed
    );
  }

  getTasksByDay(date: Date): Task[] {
    const dateStr = this.dateToStr(date);
    return this.tasksByDate.get(dateStr)?.pending || [];
  }

  getCompletedTasksByDay(date: Date): Task[] {
    const dateStr = this.dateToStr(date);
    return this.tasksByDate.get(dateStr)?.completed || [];
  }

  getFilteredTasks(): Task[] {
    const query = this.searchQuery().toLowerCase().trim();
    const tasks = this.getUnscheduledTasks();
    if (!query) return tasks;
    return tasks.filter(t => 
      t.title.toLowerCase().includes(query) ||
      t.description?.toLowerCase().includes(query)
    );
  }

  getTotalTasks(): number {
    return this.taskService.tasks().length;
  }

  getToday(): string {
    const today = new Date();
    return today.toLocaleDateString('en-US', { weekday: 'long', month: 'long', day: 'numeric' });
  }

  loadTasks() {
    this.isLoading.set(true);
    this.taskService.getTasks().subscribe({
      next: () => {
        this.isLoading.set(false);
        this.refreshTasksByDate();
      },
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

  editTask(task: Task) {
    this.taskModal.show(task);
  }
}