import { Routes } from '@angular/router';
import { RegisterPage } from './pages/register/register';
import { LoginPage } from './pages/login/login';
import { DashboardLayout } from './pages/dashboard/dashboard-layout';
import { InboxPage } from './pages/dashboard/pages/inbox/inbox';
import { PlanPage } from './pages/dashboard/pages/plan/plan';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: 'register', component: RegisterPage },
  { path: 'login', component: LoginPage },
  { 
    path: 'dashboard', 
    component: DashboardLayout, 
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'inbox', pathMatch: 'full' },
      { path: 'inbox', component: InboxPage },
      { path: 'plan', component: PlanPage }
    ]
  },
  { path: '', redirectTo: '/login', pathMatch: 'full' }
];
