import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface Task {
  id: number;
  title: string;
  description: string | null;
  dayOfWeek: 'LUNES' | 'MARTES' | 'MIERCOLES' | 'JUEVES' | 'VIERNES' | 'SABADO' | 'DOMINGO';
  time: string | null;
  completed: boolean;
  userId: number;
}

export interface TaskCreateRequest {
  title: string;
  description?: string;
  dayOfWeek: Task['dayOfWeek'];
  time?: string;
}

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private http = inject(HttpClient);
  private readonly API_URL = 'http://localhost:8080/api/tasks';

  tasks = signal<Task[]>([]);

  getTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.API_URL}`).pipe(
      tap(tasks => this.tasks.set(tasks))
    );
  }

  toggleComplete(taskId: number): Observable<Task> {
    return this.http.patch<Task>(`${this.API_URL}/${taskId}/complete`, {}).pipe(
      tap(updatedTask => {
        this.tasks.update(tasks => 
          tasks.map(t => t.id === taskId ? updatedTask : t)
        );
      })
    );
  }

  getPendingTasks(): Task[] {
    return this.tasks().filter(t => !t.completed);
  }

  getCompletedTasks(): Task[] {
    return this.tasks().filter(t => t.completed);
  }
}