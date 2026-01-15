import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private apiUrl = `${environment.apiUrl}/chat`;

  constructor(private http: HttpClient) { }

  preguntar(pregunta: string): Observable<{ respuesta: string }> {
    return this.http.post<{ respuesta: string }>(
      `${this.apiUrl}/preguntar`,
      { pregunta }
    );
  }

  // ✅ Endpoint ANTIGUO (para compatibilidad)
  descargarRespuesta(pregunta: string, respuesta: string): Observable<Blob> {
    return this.http.post(
      `${this.apiUrl}/descargar-respuesta`,
      { pregunta, respuesta },
      { responseType: 'blob' }
    );
  }

  // ✅ NUEVO: Descargar conversación completa
  descargarConversacionCompleta(mensajes: Array<{ tipo: string, texto: string }>): Observable<Blob> {
    return this.http.post(
      `${this.apiUrl}/descargar-conversacion`,
      { mensajes },
      { responseType: 'blob' }
    );
  }
}
