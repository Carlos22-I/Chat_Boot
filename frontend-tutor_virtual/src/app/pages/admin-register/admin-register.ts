import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, Validators, AbstractControl, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { EmailValidatorService, EmailValidationResult } from '../../services/email-validator'; // ✅ IMPORTAR con interface
import { Navbar } from "../../shared/navbar/navbar";

@Component({
  selector: 'app-register',
  standalone: true,
  templateUrl: './admin-register.html',
  styleUrls: ['./admin-register.scss'],
  imports: [ReactiveFormsModule, CommonModule, Navbar]
})
export class AdminRegister {

  private authService = inject(AuthService);
  private emailValidator = inject(EmailValidatorService); //  INYECTAR
  private router = inject(Router);
  private fb = inject(FormBuilder);

  isValidatingEmail = false; //  Estado de validación
  emailValidationMessage = ''; //  Mensaje de validación
  isEmailDomainValid = false; // ✅ Nuevo: Estado de dominio
  isDomainFormatChecked = false; // ✅ Nuevo: Para saber si ya se validó el formato

  registerForm = this.fb.group({
    nombres: ['', [Validators.required, Validators.minLength(3)]],
    usuario: ['', [Validators.required, Validators.minLength(4)]],
    correo: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    confirmPassword: ['', [Validators.required]],
    claveTutor: ['', [Validators.required, this.validarClaveTutor]]
  }, {
    validators: this.passwordsIguales
  });

  validarClaveTutor(control: AbstractControl) {
    const value = control.value;
    return value === 'Tutorvirtual123@' ? null : { invalidTutorKey: true };
  }

  passwordsIguales(form: AbstractControl) {
    const pass = form.get('password')?.value;
    const confirm = form.get('confirmPassword')?.value;
    return pass === confirm ? null : { passwordMismatch: true };
  }

  //  NUEVO: Validar correo cuando el usuario termina de escribir
  onEmailChange(): void {
    const correoControl = this.registerForm.get('correo');
    const correo = correoControl?.value?.toLowerCase() || '';

    // Resetear mensaje
    this.emailValidationMessage = '';
    this.isEmailDomainValid = false;
    this.isDomainFormatChecked = true;

    // Si el campo está vacío o no tiene formato de email básico, no validar
    if (!correo || correoControl?.hasError('email')) {
      return;
    }

    // 1️⃣ Validar Dominios Permitidos (@gmail.com y @unamba.edu.pe)
    const allowedDomains = ['@gmail.com', '@unamba.edu.pe'];
    this.isEmailDomainValid = allowedDomains.some(domain => correo.endsWith(domain));

    if (!this.isEmailDomainValid) {
      this.emailValidationMessage = '❌ Formato incorrecto';
      correoControl?.setErrors({ domainInvalid: true });
      return;
    }

    this.emailValidationMessage = '✅ Formato correcto';

    // 2️⃣ Validar si el correo ya existe (Llamada al backend)
    this.isValidatingEmail = true;

    this.emailValidator.validarCorreo(correo).subscribe({
      next: (result: EmailValidationResult) => {
        this.isValidatingEmail = false;

        if (result.valido) {
          // El backend dice que el correo es válido (no existe)
          this.emailValidationMessage = '✅ Formato correcto';
          correoControl?.setErrors(null);
        } else {
          // El correo ya existe
          this.emailValidationMessage = '❌ ' + result.mensaje;
          correoControl?.setErrors({ emailInvalido: true });
        }
      },
      error: (err: any) => {
        this.isValidatingEmail = false;
        this.emailValidationMessage = '⚠️ No se pudo verificar si el correo existe';
        console.error('Error validando correo:', err);
      }
    });
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    // Verificar que el correo fue validado
    const correoControl = this.registerForm.get('correo');
    if (correoControl?.hasError('emailInvalido')) {
      alert('❌ Por favor ingresa un correo válido');
      return;
    }

    // 🔥 MAPEO CLAVE (Angular → Backend)
    const payload = {
      apellidosNombres: this.registerForm.value.nombres,
      nombreUsuario: this.registerForm.value.usuario,
      correo: this.registerForm.value.correo,
      contrasena: this.registerForm.value.password,
      confirmarContrasena: this.registerForm.value.confirmPassword
    };

    this.authService.register(payload).subscribe({
      next: () => {
        alert(' registro exitoso ');
        this.router.navigate(['/admin-login']);
      },
      error: (err: any) => { //  Tipado
        console.error(err);
        const errorMessage = err.error?.error || 'Error al registrar usuario';
        alert(errorMessage);
      }
    });
    console.log('PAYLOAD FINAL:', payload);
  }

  goLogin(): void {
    this.router.navigate(['/admin-login']);
  }
}