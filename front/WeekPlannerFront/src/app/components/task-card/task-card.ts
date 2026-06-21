import { Component, ChangeDetectionStrategy, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-task-card',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule],
  templateUrl: './task-card.html'
})
export class TaskCardComponent {
  title = input.required<string>();
  executionDate = input<string | null>(null);
  time = input<string | null>(null);
  description = input<string | null>(null);
  completed = input(false);
  variant = input<'inbox' | 'unscheduled' | 'column' | 'completed'>('inbox');
  accentColor = input<'primary' | 'secondary' | 'tertiary'>('primary');

  toggle = output<MouseEvent>();
  edit = output<MouseEvent>();

  onToggle(event: MouseEvent) {
    event.stopPropagation();
    this.toggle.emit(event);
  }

  onEdit(event: MouseEvent) {
    this.edit.emit(event);
  }
}
