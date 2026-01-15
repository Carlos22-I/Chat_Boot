import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map, catchError, timeout } from 'rxjs/operators';

// ✅ Interface exportada
export interface EmailValidationResult {
  valido: boolean;
  mensaje: string;
  existe?: boolean;
  esDisposable?: boolean;
  detalles?: any;
}

@Injectable({
  providedIn: 'root'
})
export class EmailValidatorService {

  // ✅ COLOCA TU API KEY AQUÍ (Obtenla de https://app.abstractapi.com)
  private readonly API_KEY = 'TU_API_KEY_AQUI';
  private readonly API_URL = 'https://emailvalidation.abstractapi.com/v1/';

  constructor(private http: HttpClient) {}

  // ✅ Método principal de validación
  validarCorreo(email: string): Observable<EmailValidationResult> {
    // Si no hay API key, usar validación local
    if (!this.API_KEY || this.API_KEY === 'TU_API_KEY_AQUI') {
      console.warn('⚠️ API Key no configurada, usando validación local');
      return of(this.validarLocal(email));
    }

    const url = `${this.API_URL}?api_key=${this.API_KEY}&email=${encodeURIComponent(email)}`;

    return this.http.get<any>(url).pipe(
      timeout(5000), // Timeout de 5 segundos
      map(response => {
        console.log('📧 Respuesta API:', response);

        // Verificar si el correo es válido
        const formatoValido = response.is_valid_format?.value === true;
        const esDisposable = response.is_disposable_email?.value === true;
        const noEntregable = response.deliverability === 'UNDELIVERABLE';
        const existeSMTP = response.is_smtp_valid?.value === true;

        let valido = formatoValido && !esDisposable && !noEntregable;
        let mensaje = '';

        if (!formatoValido) {
          mensaje = 'El formato del correo es inválido';
          valido = false;
        } else if (esDisposable) {
          mensaje = 'No se permiten correos temporales o desechables';
          valido = false;
        } else if (noEntregable) {
          mensaje = 'El correo no puede recibir mensajes';
          valido = false;
        } else if (!existeSMTP) {
          mensaje = 'El correo no existe o no está activo';
          valido = false;
        } else {
          mensaje = '✅ Correo válido y verificado';
          valido = true;
        }

        return {
          valido,
          mensaje,
          existe: existeSMTP,
          esDisposable,
          detalles: response
        };
      }),
      catchError((error: any) => {
        console.error('❌ Error en API:', error);
        
        // Si falla la API, usar validación local como fallback
        if (error.status === 429) {
          return of({
            valido: false,
            mensaje: '⚠️ Límite de validaciones alcanzado. Intenta más tarde.',
            detalles: error
          });
        }

        console.warn('⚠️ Usando validación local por error en API');
        return of(this.validarLocal(email));
      })
    );
  }

  // ✅ Validación LOCAL (backup cuando la API falla)
  private validarLocal(email: string): EmailValidationResult {
    if (!this.validarFormato(email)) {
      return {
        valido: false,
        mensaje: 'Formato de correo inválido'
      };
    }

    if (this.esCorreoTemporal(email)) {
      return {
        valido: false,
        mensaje: 'No se permiten correos temporales'
      };
    }

    return {
      valido: true,
      mensaje: 'Formato correcto ',
      detalles: { modo: 'local' }
    };
  }

  // Validar formato
  private validarFormato(email: string): boolean {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
  }

  // Lista de dominios temporales conocidos
  private esCorreoTemporal(email: string): boolean {
    const dominiosTemporales = [
      'tempmail.com', 'guerrillamail.com', '10minutemail.com',
      'mailinator.com', 'throwaway.email', 'temp-mail.org',
      'maildrop.cc', 'sharklasers.com', 'yopmail.com',
      'trashmail.com', 'getnada.com', 'mohmal.com',
      'fakeinbox.com', 'dispostable.com', 'emailondeck.com'
    ];

    const dominio = email.split('@')[1]?.toLowerCase();
    return dominiosTemporales.some(temp => dominio?.includes(temp));
  }
}