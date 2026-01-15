import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DocumentoService } from '../../services/documento.service';
import { Router } from '@angular/router';

interface DocumentFile {
  id: number;
  name: string;
  date: string;
  size: string;
  category: string;
}

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.html',
  styleUrls: ['./admin-dashboard.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class AdminDashboard implements OnInit {

  constructor(
    private documentoService: DocumentoService,
    private router: Router
  ) {}

  files: DocumentFile[] = [];
  filesFiltrados: DocumentFile[] = [];
  
  busqueda = '';
  filtroCategoria = 'Todos';
  categoriaSeleccionada = 'General';
  
  isLoading = false;
  isDragging = false; // ✅ NUEVO: Control de arrastre

  ngOnInit(): void {
    this.cargarDocumentos();
  }

  cargarDocumentos(mantenerScroll: boolean = false) {
    // ✅ Guardar posición actual del scroll
    const scrollPosition = mantenerScroll ? window.scrollY : 0;
    
    this.documentoService.listarDocumentos().subscribe({
      next: (data: any[]) => {
        console.log('📥 Documentos recibidos:', data);
        
        this.files = data.map(doc => ({
          id: doc.id,
          name: doc.nombreArchivo,
          date: doc.fechaSubida,
          size: `${doc.tamañoArchivo.toFixed(2)} MB`,
          category: doc.categoria
        }));
        
        this.filesFiltrados = this.files;
        console.log('✅ Documentos cargados:', this.files.length);
        
        // ✅ Restaurar posición del scroll después de que Angular actualice la vista
        if (mantenerScroll) {
          setTimeout(() => {
            window.scrollTo(0, scrollPosition);
          }, 0);
        }
      },
      error: (err) => {
        console.error('❌ Error al cargar documentos:', err);
        alert('Error al cargar documentos');
      }
    });
  }

  // ✅ NUEVO: Drag & Drop - Prevenir comportamiento por defecto
  onDragOver(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    this.isDragging = true;
  }

  // ✅ NUEVO: Drag & Drop - Cuando sale del área
  onDragLeave(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    this.isDragging = false;
  }

  // ✅ NUEVO: Drag & Drop - Al soltar archivos
  async onDrop(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    this.isDragging = false;

    const files = event.dataTransfer?.files;
    if (!files || files.length === 0) return;

    // Convertir FileList a Array<File> correctamente
    const fileArray: File[] = [];
    for (let i = 0; i < files.length; i++) {
      const file = files[i];
      if (file) {
        fileArray.push(file);
      }
    }

    // Filtrar solo PDFs
    const pdfFiles = fileArray.filter(file => file.type === 'application/pdf');

    if (pdfFiles.length === 0) {
      alert('⚠️ Solo se permiten archivos PDF');
      return;
    }

    if (pdfFiles.length !== files.length) {
      alert(`⚠️ Se ignoraron ${files.length - pdfFiles.length} archivo(s) que no son PDF`);
    }

    console.log(`📤 Subiendo ${pdfFiles.length} archivo(s) por drag & drop...`);
    await this.subirMultiplesArchivos(pdfFiles);
  }

  // ✅ MEJORADO: Subir múltiples archivos con feedback
  async onFile(event: any) {
    const files = event.target.files;
    if (!files || files.length === 0) return;

    // Convertir FileList a Array<File> correctamente
    const fileArray: File[] = [];
    for (let i = 0; i < files.length; i++) {
      const file = files[i];
      if (file) {
        fileArray.push(file);
      }
    }

    // Filtrar solo PDFs
    const pdfFiles = fileArray.filter(file => file.type === 'application/pdf');

    if (pdfFiles.length === 0) {
      alert('⚠️ Solo se permiten archivos PDF');
      event.target.value = '';
      return;
    }

    console.log(`📤 Subiendo ${pdfFiles.length} archivo(s)...`);
    await this.subirMultiplesArchivos(pdfFiles);

    // Reset input
    event.target.value = '';
  }

  // ✅ NUEVO: Método unificado para subir múltiples archivos
  private async subirMultiplesArchivos(files: File[]) {
    this.isLoading = true;

    let subidosExitosamente = 0;
    let errores = 0;

    // Subir archivos uno por uno
    for (const file of files) {
      try {
        await this.subirArchivoPromise(file);
        subidosExitosamente++;
        console.log(`✅ Subido: ${file.name}`);
      } catch (error) {
        errores++;
        console.error(`❌ Error al subir ${file.name}:`, error);
      }
    }

    // ✅ Recargar documentos MANTENIENDO la posición del scroll
    this.cargarDocumentos(true);
    this.isLoading = false;

    // Mensaje final
    if (errores === 0) {
      alert(`✅ ${subidosExitosamente} documento(s) subido(s) correctamente`);
    } else {
      alert(`⚠️ Subidos: ${subidosExitosamente}, Errores: ${errores}`);
    }
  }

  // ✅ Convertir observable a promesa
  private subirArchivoPromise(file: File): Promise<any> {
    return new Promise((resolve, reject) => {
      this.documentoService
        .subirDocumento(file, this.categoriaSeleccionada)
        .subscribe({
          next: (response) => resolve(response),
          error: (error) => reject(error)
        });
    });
  }

  eliminar(id: number) {
    if (!confirm('¿Estás seguro de eliminar este documento?')) return;

    console.log('🗑️ Eliminando documento ID:', id);

    this.documentoService.eliminarDocumento(id).subscribe({
      next: () => {
        console.log('✅ Documento eliminado');
        // ✅ Mantener posición del scroll al recargar
        this.cargarDocumentos(true);
        alert('Documento eliminado correctamente');
      },
      error: (err) => {
        console.error('❌ Error al eliminar:', err);
        alert('Error al eliminar documento');
      }
    });
  }

  verDocumento(id: number) {
    console.log('👁️ Viendo documento ID:', id);
    window.open(`http://localhost:8080/api/documentos/ver/${id}`, '_blank');
  }

  descargarDocumento(id: number) {
    console.log('⬇️ Descargando documento ID:', id);
    window.open(`http://localhost:8080/api/documentos/descargar/${id}`, '_blank');
  }

  seleccionarArchivoActualizar(id: number) {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = 'application/pdf';

    input.onchange = async (event: any) => {
      const archivo = event.target.files[0];
      if (!archivo) return;

      console.log('🔄 Actualizando documento ID:', id);
      this.isLoading = true;

      this.documentoService
        .actualizarDocumento(id, archivo, this.categoriaSeleccionada)
        .subscribe({
          next: () => {
            console.log('✅ Documento actualizado');
            // ✅ Mantener posición del scroll al recargar
            this.cargarDocumentos(true);
            this.isLoading = false;
            alert('Documento actualizado correctamente');
          },
          error: (err) => {
            console.error('❌ Error al actualizar:', err);
            this.isLoading = false;
            alert('Error al actualizar documento');
          }
        });
    };

    input.click();
  }

  totalCategorias(): number {
    return new Set(this.files.map(f => f.category)).size;
  }

  documentosSubidosHoy(): number {
    const hoy = new Date().toISOString().split('T')[0];
    return this.files.filter(f => f.date === hoy).length;
  }

  filtrar() {
    console.log('🔍 Filtrando:', {
      busqueda: this.busqueda,
      categoria: this.filtroCategoria
    });

    this.filesFiltrados = this.files.filter(f => {
      const matchBusqueda = f.name.toLowerCase().includes(this.busqueda.toLowerCase());
      const matchCategoria = this.filtroCategoria === 'Todos' || f.category === this.filtroCategoria;
      return matchBusqueda && matchCategoria;
    });

    console.log('📋 Resultados filtrados:', this.filesFiltrados.length);
  }

  logout(): void {
    localStorage.removeItem('token');
    this.router.navigate(['/']);
  }
}