import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DocumentoService {

  private apiUrl = `${environment.apiUrl}/documentos`;

  constructor(private http: HttpClient) { }

  // 📌 Subir PDF
  subirDocumento(file: File, categoria: string): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('categoria', categoria);

    return this.http.post(`${this.apiUrl}/upload`, formData);
  }

  // 📌 Listar documentos
  listarDocumentos(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  // 📌 Eliminar documento
  eliminarDocumento(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  // 📌 Actualizar documento
  actualizarDocumento(id: number, file: File, categoria: string) {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('categoria', categoria);

    return this.http.put(`${this.apiUrl}/${id}`, formData);
  }
}
