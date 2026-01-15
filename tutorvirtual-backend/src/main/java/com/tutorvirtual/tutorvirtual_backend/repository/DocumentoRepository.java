package com.tutorvirtual.tutorvirtual_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tutorvirtual.tutorvirtual_backend.entity.Documento;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    
    // ✅ OPCIÓN 1: Query nativa (más confiable)
    @Query(value = "SELECT COUNT(*) FROM documentos WHERE fecha_subida = :fecha", nativeQuery = true)
    long contarPorFecha(@Param("fecha") String fecha);
    
    // ✅ OPCIÓN 2: Query JPQL (alternativa)
    @Query("SELECT COUNT(d) FROM Documento d WHERE d.fechaSubida = :fecha")
    long contarPorFechaJPQL(@Param("fecha") String fecha);
}