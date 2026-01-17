package com.tutorvirtual.tutorvirtual_backend.service.documento;

import java.time.LocalDate;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tutorvirtual.tutorvirtual_backend.dto.DocumentoDTO;
import com.tutorvirtual.tutorvirtual_backend.entity.Documento;
import com.tutorvirtual.tutorvirtual_backend.repository.DocumentoRepository;

@Service
public class DocumentoService {

    private final DocumentoRepository documentoRepository;

    public DocumentoService(DocumentoRepository documentoRepository) {
        this.documentoRepository = documentoRepository;
    }

    @Transactional(timeout = 120) // 2 minutos para archivos grandes
    @CacheEvict(value = "documentos", allEntries = true)
    public Documento guardarDocumento(Documento documento) {
        return documentoRepository.save(documento);
    }

    public boolean existePorNombre(String nombre) {
        return documentoRepository.existsByNombreArchivo(nombre);
    }

    @CacheEvict(value = "documentos", allEntries = true)
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

    public long contarDocumentosHoy() {
        String fechaHoy = LocalDate.now().toString();
        return documentoRepository.contarPorFecha(fechaHoy);
    }

    @Cacheable("documentos")
    public List<DocumentoDTO> listarDocumentos() {
        return documentoRepository.findAllOptimized();
    }
}