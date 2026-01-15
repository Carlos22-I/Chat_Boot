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

  registerForm = this.fb.group({
    nombres: ['', [Validators.required, Validators.minLength(3)]],
    usuario: ['', [Validators.required, Validators.minLength(4)]],
    correo: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    confirmPassword: ['', [Validators.required]]
  }, {
    validators: this.passwordsIguales
  });

  passwordsIguales(form: AbstractControl) {
    const pass = form.get('password')?.value;
    const confirm = form.get('confirmPassword')?.value;
    return pass === confirm ? null : { passwordMismatch: true };
  }

  //  NUEVO: Validar correo cuando el usuario termina de escribir
  onEmailChange(): void {
    const correoControl = this.registerForm.get('correo');
    const correo = correoControl?.value;

    // Resetear mensaje
    this.emailValidationMessage = '';

    // Si el campo está vacío o inválido, no validar
    if (!correo || correoControl?.invalid) {
      return;
    }

    // Mostrar que está validando
    this.isValidatingEmail = true;
    this.emailValidationMessage = '🔍 Verificando correo...';

    // Llamar al servicio de validación
    this.emailValidator.validarCorreo(correo).subscribe({
      next: (result: EmailValidationResult) => { // Tipado
        this.isValidatingEmail = false;
        
        if (result.valido) {
          this.emailValidationMessage = '✅ ' + result.mensaje;
          correoControl?.setErrors(null); // Limpiar errores
        } else {
          this.emailValidationMessage = '❌ ' + result.mensaje;
          correoControl?.setErrors({ emailInvalido: true }); // Marcar como inválido
        }
      },
      error: (err: any) => { // Tipado
        this.isValidatingEmail = false;
        this.emailValidationMessage = '⚠️ No se pudo verificar el correo';
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
      contraseña: this.registerForm.value.password,
      confirmarContraseña: this.registerForm.value.confirmPassword
    };

    this.authService.register(payload).subscribe({
      next: () => {
        alert(' registro exitoso ');
        this.router.navigate(['/admin-login']);
      },
      error: (err: any) => { //  Tipado
        console.error(err);
        alert(err.error || 'Error al registrar usuario');
      }
    });
    console.log('PAYLOAD FINAL:', payload);
  }

  goLogin(): void {
    this.router.navigate(['/admin-login']);
  }
}