package com.tutorvirtual.tutorvirtual_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tutorvirtual.tutorvirtual_backend.entity.Documento;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    // ✅ Verificar si ya existe un archivo con este nombre
    boolean existsByNombreArchivo(String nombreArchivo);

    // ✅ Contar documentos por fecha exacta
    @Query("SELECT COUNT(d) FROM Documento d WHERE d.fechaSubida = :fecha")
    long contarPorFecha(@Param("fecha") String fecha);
}