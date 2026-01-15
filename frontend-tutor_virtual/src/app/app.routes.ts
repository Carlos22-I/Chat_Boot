import { Routes } from '@angular/router';

import { Home } from './pages/home/home';
import { AdminLogin } from './pages/home/admin-login/admin-login';
import { AdminRegister } from './pages/admin-register/admin-register';
import { AdminDashboard } from './pages/admin-dashboard/admin-dashboard';
import { UploadDocuments } from './pages/upload-documents/upload-documents';
import { ChatTutor } from './pages/chat-tutor/chat-tutor';

import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', component: Home },
  { path: '', redirectTo: 'admin-login', pathMatch: 'full' },

  { path: 'admin-login', component: AdminLogin },
  { path: 'admin-register', component: AdminRegister },

  {
    path: 'admin-dashboard',
    component: AdminDashboard,
    canActivate: [AuthGuard]
  },
  {
    path: 'admin-upload',
    component: UploadDocuments,
    canActivate: [AuthGuard]
  },

  { path: 'chat-tutor', component: ChatTutor },

  { path: '**', redirectTo: '' }
];
