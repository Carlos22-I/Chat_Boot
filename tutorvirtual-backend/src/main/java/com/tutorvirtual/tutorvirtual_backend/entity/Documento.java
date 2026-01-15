package com.tutorvirtual.tutorvirtual_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "documentos")
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nombre_archivo", nullable = false)
    private String nombreArchivo;

    @Column(name = "tamaño_archivo", nullable = false)
    private float tamañoArchivo;

    @Column(nullable = false)
    private String categoria;

    @Column(name = "fecha_subida", nullable = false)
    private String fechaSubida;

    @Column(name = "ruta_archivo", nullable = false)
    private String rutaArchivo;

    @Column(columnDefinition = "TEXT")
    private String contenidoTexto;

    public String getContenidoTexto() {
        return contenidoTexto;
    }

    public void setContenidoTexto(String contenidoTexto) {
        this.contenidoTexto = contenidoTexto;
    }

    private String geminiFileId;

    public String getGeminiFileId() {
        return geminiFileId;
    }

    public void setGeminiFileId(String geminiFileId) {
        this.geminiFileId = geminiFileId;
    }

    @Column(name = "datos")
    private byte[] datos;

    public byte[] getDatos() {
        return datos;
    }

    public void setDatos(byte[] datos) {
        this.datos = datos;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public float getTamañoArchivo() {
        return tamañoArchivo;
    }

    public void setTamañoArchivo(float tamañoArchivo) {
        this.tamañoArchivo = tamañoArchivo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(String fechaSubida) {
        this.fechaSubida = fechaSubida;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }
}
