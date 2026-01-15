package com.tutorvirtual.tutorvirtual_backend.service.documento;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tutorvirtual.tutorvirtual_backend.entity.Documento;
import com.tutorvirtual.tutorvirtual_backend.repository.DocumentoRepository;

@Service
public class DocumentoService {

    private final DocumentoRepository documentoRepository;

    public DocumentoService(DocumentoRepository documentoRepository) {
        this.documentoRepository = documentoRepository;
    }

    public Documento guardarDocumento(Documento documento) {
        return documentoRepository.save(documento);
    }

    public void eliminarDocumento(Long id) {
        documentoRepository.deleteById(id);
    }

    public Documento obtenerPorId(Long id) {
        return documentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));
    }

    public Documento buscarPorId(Long id) {
        return documentoRepository.findById(id).orElse(null);
    }

    // ✅ MÉTODO CORREGIDO - MÁS ROBUSTO
    public long contarDocumentosHoy() {
        String fechaHoy = LocalDate.now().toString();
        
        System.out.println("🔍 Buscando documentos con fecha: " + fechaHoy);
        
        // Intentar con query nativa primero
        long cantidad = documentoRepository.contarPorFecha(fechaHoy);
        
        System.out.println("📊 Documentos encontrados hoy: " + cantidad);
        
        // Si no encuentra nada, contar manualmente como fallback
        if (cantidad == 0) {
            List<Documento> todos = documentoRepository.findAll();
            cantidad = todos.stream()
                .filter(d -> d.getFechaSubida() != null && d.getFechaSubida().startsWith(fechaHoy))
                .count();
            
            System.out.println("📊 Documentos encontrados hoy (manual): " + cantidad);
        }
        
        return cantidad;
    }

    public List<Documento> listarDocumentos() {
        return documentoRepository.findAll();
    }
}