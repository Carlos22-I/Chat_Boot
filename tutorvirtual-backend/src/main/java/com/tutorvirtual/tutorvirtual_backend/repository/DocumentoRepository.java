package com.tutorvirtual.tutorvirtual_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.tutorvirtual.tutorvirtual_backend.dto.DocumentoDTO;

import com.tutorvirtual.tutorvirtual_backend.entity.Documento;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    // ✅ Verificar si ya existe un archivo con este nombre
    boolean existsByNombreArchivo(String nombreArchivo);

    // ✅ Contar documentos por fecha exacta
    @Query("SELECT COUNT(d) FROM Documento d WHERE d.fechaSubida = :fecha")
    long contarPorFecha(@Param("fecha") String fecha);

    // ✅ Listar documentos optimizado (sin bytes)
    @Query("SELECT new com.tutorvirtual.tutorvirtual_backend.dto.DocumentoDTO(d.id, d.nombreArchivo, d.tamanoArchivo, d.categoria, d.fechaSubida) FROM Documento d")
    List<DocumentoDTO> findAllOptimized();

    // ✅ Obtener solo texto para Gemini (evitar cargar bytes)
    @Query("SELECT d.nombreArchivo, d.contenidoTexto FROM Documento d")
    List<Object[]> findForGeminiContext();
}