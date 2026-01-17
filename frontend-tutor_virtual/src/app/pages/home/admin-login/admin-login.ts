import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Navbar } from "../../../shared/navbar/navbar";
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-admin-login',
  templateUrl: './admin-login.html',
  styleUrl: './admin-login.scss',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, Navbar]
})
export class AdminLogin implements OnInit {

  loginForm!: FormGroup;
  errorMessage = '';
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService
  ) { }

  ngOnInit() {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],  // ✅
      password: ['', Validators.required]
    });
  }

  goRegister() {
    this.router.navigate(['/admin-register']);
  }

  onLogin() {
    if (this.loginForm.invalid) {
      this.errorMessage = 'Por favor completa correctamente los campos';
      return;
    }

    this.errorMessage = '';
    this.isLoading = true;

    const loginData = {
      email: this.loginForm.value.email,
      contrasena: this.loginForm.value.password
    };

    this.authService.login(loginData).subscribe({
      next: (response) => {
        this.authService.saveToken(response.token);
        this.router.navigate(['/admin-dashboard']);
      },
      error: (err) => {
        this.errorMessage = err.error?.error || 'Usuario o contraseña incorrectos';
        this.isLoading = false;
      }
    });
  }
}