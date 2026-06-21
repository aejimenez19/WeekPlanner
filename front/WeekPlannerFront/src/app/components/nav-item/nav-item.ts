import { Component, ChangeDetectionStrategy, input } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-nav-item',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './nav-item.html'
})
export class NavItemComponent {
  icon = input.required<string>();
  label = input.required<string>();
  route = input.required<string>();
  exact = input(false);
  variant = input<'desktop' | 'mobile'>('desktop');
  filled = input(false);
}
