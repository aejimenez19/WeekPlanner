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

  readonly daysOfWeek = ['LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO', 'DOMINGO'];
  readonly daysEnglish = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];

  private getWeekDates(): { date: string; display: string }[] {
    const today = new Date();
    const currentDay = today.getDay();
    const monday = new Date(today);
    monday.setDate(today.getDate() - (currentDay === 0 ? 6 : currentDay - 1));
    
    return this.daysEnglish.map((day, index) => {
      const date = new Date(monday);
      date.setDate(monday.getDate() + index);
      const month = date.getMonth() + 1;
      const dayNum = date.getDate();
      return {
        date: `${month < 10 ? '0' + month : month}/${dayNum < 10 ? '0' + dayNum : dayNum}`,
        display: day
      };
    });
  }

  get weekDates() {
    return this.getWeekDates();
  }

  ngOnInit() {
    this.loadTasks();
  }
  
  openNewTaskModal() {
    this.taskModal.show();
  }

  getTasksByDay(day: string): Task[] {
    return this.taskService.tasks().filter(t => t.dayOfWeek === day && !t.completed);
  }

  getCompletedTasksByDay(day: string): Task[] {
    return this.taskService.tasks().filter(t => t.dayOfWeek === day && t.completed);
  }

  getPendingTasks(): Task[] {
    return this.taskService.tasks().filter(t => !t.completed);
  }

  getFilteredTasks(): Task[] {
    const query = this.searchQuery().toLowerCase().trim();
    if (!query) return this.getPendingTasks();
    return this.getPendingTasks().filter(t => 
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