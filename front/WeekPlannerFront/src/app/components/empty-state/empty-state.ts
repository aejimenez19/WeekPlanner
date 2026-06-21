import { Component, ChangeDetectionStrategy, input } from '@angular/core';

@Component({
  selector: 'app-empty-state',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './empty-state.html'
})
export class EmptyStateComponent {
  icon = input('spa');
  quote = input("Simplicity is the ultimate sophistication.");
  author = input('Leonardo da Vinci');
}
