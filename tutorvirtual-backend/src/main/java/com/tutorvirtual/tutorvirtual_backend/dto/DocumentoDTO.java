package com.tutorvirtual.tutorvirtual_backend.dto;

public class DocumentoDTO {
    private Long id;
    private String nombreArchivo;
    private float tamanoArchivo;
    private String categoria;
    private String fechaSubida;

    public DocumentoDTO(Long id, String nombreArchivo, float tamanoArchivo, String categoria, String fechaSubida) {
        this.id = id;
        this.nombreArchivo = nombreArchivo;
        this.tamanoArchivo = tamanoArchivo;
        this.categoria = categoria;
        this.fechaSubida = fechaSubida;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public float getTamanoArchivo() {
        return tamanoArchivo;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getFechaSubida() {
        return fechaSubida;
    }
}
