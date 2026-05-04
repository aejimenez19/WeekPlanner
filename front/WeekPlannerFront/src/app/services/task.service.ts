import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Task {
  id: number;
  title: string;
  description: string | null;
  executionDate: string | null;
  time: string | null;
  completed: boolean;
  userId: number;
}

export interface TaskCreateRequest {
  title: string;
  description?: string;
  executionDate?: string;
  time?: string;
}

export interface TaskUpdateRequest {
  title?: string;
  description?: string;
  executionDate?: string;
  time?: string;
  completed?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private http = inject(HttpClient);
  private readonly API_URL = environment.API_URL + '/api/tasks';

  tasks = signal<Task[]>([]);

  getTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.API_URL}`).pipe(
      tap(tasks => this.tasks.set(tasks))
    );
  }

  toggleComplete(taskId: number, completed: boolean): Observable<Task> {
    return this.http.put<Task>(`${this.API_URL}/${taskId}`, { completed }).pipe(
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

  createTask(task: TaskCreateRequest): Observable<Task> {
    return this.http.post<Task>(`${this.API_URL}`, task).pipe(
      tap(newTask => {
        this.tasks.update(tasks => [...tasks, newTask]);
      })
    );
  }

  updateTask(taskId: number, task: TaskUpdateRequest): Observable<Task> {
    return this.http.put<Task>(`${this.API_URL}/${taskId}`, task).pipe(
      tap(updatedTask => {
        this.tasks.update(tasks => 
          tasks.map(t => t.id === taskId ? updatedTask : t)
        );
      })
    );
  }
}