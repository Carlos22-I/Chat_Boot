import { Routes } from '@angular/router';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/home/home').then(m => m.Home)
  },
  { path: '', redirectTo: 'admin-login', pathMatch: 'full' },

  {
    path: 'admin-login',
    loadComponent: () => import('./pages/home/admin-login/admin-login').then(m => m.AdminLogin)
  },
  {
    path: 'admin-register',
    loadComponent: () => import('./pages/admin-register/admin-register').then(m => m.AdminRegister)
  },

  {
    path: 'admin-dashboard',
    loadComponent: () => import('./pages/admin-dashboard/admin-dashboard').then(m => m.AdminDashboard),
    canActivate: [AuthGuard]
  },
  {
    path: 'admin-upload',
    loadComponent: () => import('./pages/upload-documents/upload-documents').then(m => m.UploadDocuments),
    canActivate: [AuthGuard]
  },

  {
    path: 'chat-tutor',
    loadComponent: () => import('./pages/chat-tutor/chat-tutor').then(m => m.ChatTutor)
  },

  { path: '**', redirectTo: '' }
];
