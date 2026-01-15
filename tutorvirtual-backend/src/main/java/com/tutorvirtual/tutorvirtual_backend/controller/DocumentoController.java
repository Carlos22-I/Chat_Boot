package com.tutorvirtual.tutorvirtual_backend.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tutorvirtual.tutorvirtual_backend.entity.Documento;
import com.tutorvirtual.tutorvirtual_backend.service.documento.DocumentoService;
import com.tutorvirtual.tutorvirtual_backend.service.pdf.PdfTextService;

@RestController
@RequestMapping("/api/documentos")
@CrossOrigin(origins = "*")
public class DocumentoController {

    private final DocumentoService documentoService;
    private final PdfTextService pdfTextService;

    public DocumentoController(
            DocumentoService documentoService,
            PdfTextService pdfTextService) {
        this.documentoService = documentoService;
        this.pdfTextService = pdfTextService;
    }

    // =========================
    // 1️⃣ LISTAR DOCUMENTOS
    // =========================
    @GetMapping
    public ResponseEntity<List<Documento>> listarDocumentos() {
        return ResponseEntity.ok(documentoService.listarDocumentos());
    }

    // =========================
    // 2️⃣ SUBIR DOCUMENTO PDF
    // =========================
    @PostMapping("/upload")
    public ResponseEntity<?> subirDocumento(
            @RequestParam("file") MultipartFile file,
            @RequestParam("categoria") String categoria) {
        try {
            if (!"application/pdf".equals(file.getContentType())) {
                return ResponseEntity.badRequest().body("Solo se permiten archivos PDF");
            }

            // 📁 Guardar archivo (REEMPLAZADO POR BD)
            // String uploadDir = "uploads/documentos/";
            String nombreArchivo = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            // Path rutaCompleta = Paths.get(uploadDir, nombreArchivo);

            // Files.createDirectories(rutaCompleta.getParent());
            // Files.write(rutaCompleta, file.getBytes());

            // 🧠 EXTRAER TEXTO DEL PDF
            String textoExtraido = pdfTextService.extraerTexto(file);

            // 💾 Guardar metadata en BD
            Documento documento = new Documento();
            documento.setNombreArchivo(nombreArchivo);
            documento.setCategoria(categoria);
            documento.setRutaArchivo("DB_STORAGE"); // Valor placeholder
            documento.setDatos(file.getBytes());
            documento.setFechaSubida(LocalDate.now().toString());
            documento.setTamañoArchivo(file.getSize() / (1024f * 1024f));
            documento.setContenidoTexto(textoExtraido);

            documentoService.guardarDocumento(documento);

            return ResponseEntity.ok(
                    Map.of("mensaje", "Documento subido correctamente"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Error al subir el documento");
        }
    }

    // =========================
    // 📌 3️⃣ ELIMINAR DOCUMENTO (✅ CORREGIDO)
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDocumento(@PathVariable Long id) {
        try {
            // 🔍 Buscar el documento en la BD
            Documento documento = documentoService.buscarPorId(id);

            if (documento == null) {
                return ResponseEntity.notFound().build();
            }

            // 🗑️ ELIMINAR ARCHIVO FÍSICO DEL SISTEMA
            // 🗑️ ELIMINAR ARCHIVO FÍSICO (YA NO ES NECESARIO SI ESTÁ EN BD)
            // Path rutaArchivo = Paths.get(documento.getRutaArchivo());

            // if (Files.exists(rutaArchivo)) {
            // Files.delete(rutaArchivo);
            // System.out.println("✅ Archivo físico eliminado: " + rutaArchivo);
            // } else {
            // System.out.println("⚠️ El archivo físico no existe: " + rutaArchivo);
            // }

            // 🗑️ ELIMINAR REGISTRO DE LA BASE DE DATOS
            documentoService.eliminarDocumento(id);

            return ResponseEntity.ok().body(
                    Map.of("mensaje", "Documento eliminado correctamente"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Error al eliminar el documento: " + e.getMessage());
        }
    }

    // =========================
    // 👁️ VER DOCUMENTO
    // =========================
    @GetMapping("/ver/{id}")
    public ResponseEntity<?> verDocumento(@PathVariable Long id) {
        Documento doc = documentoService.buscarPorId(id);

        if (doc == null) {
            return ResponseEntity.notFound().build();
        }

        // Path ruta = Paths.get(doc.getRutaArchivo());

        // if (!Files.exists(ruta)) {
        // return ResponseEntity.notFound().build();
        // }

        if (doc.getDatos() == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            byte[] archivo = doc.getDatos();

            return ResponseEntity.ok()
                    .header("Content-Type", "application/pdf")
                    .body(archivo);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // =========================
    // ⬇️ DESCARGAR DOCUMENTO
    // =========================
    @GetMapping("/descargar/{id}")
    public ResponseEntity<?> descargarDocumento(@PathVariable Long id) {
        try {
            Documento documento = documentoService.obtenerPorId(id);

            // Path path = Paths.get(documento.getRutaArchivo());

            // if (!Files.exists(path)) {
            // return ResponseEntity.notFound().build();
            // }

            if (documento.getDatos() == null) {
                return ResponseEntity.notFound().build();
            }

            byte[] archivo = documento.getDatos();

            return ResponseEntity.ok()
                    .header("Content-Disposition",
                            "attachment; filename=\"" + documento.getNombreArchivo() + "\"")
                    .body(archivo);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("No se pudo descargar el documento");
        }
    }

    // =========================
    // 🔄 ACTUALIZAR DOCUMENTO (✅ YA ESTABA BIEN)
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarDocumento(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("categoria") String categoria) {
        Documento doc = documentoService.buscarPorId(id);

        if (doc == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            // 🗑️ eliminar archivo anterior (YA NO NECESARIO)
            // Path rutaAnterior = Paths.get(doc.getRutaArchivo());
            // if (Files.exists(rutaAnterior)) { ... }

            // 📁 guardar nuevo archivo (EN BD)
            // String uploadDir = "uploads/documentos/";
            String nuevoNombre = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            // Path nuevaRuta = Paths.get(uploadDir + nuevoNombre);

            // Files.createDirectories(nuevaRuta.getParent());
            // Files.write(nuevaRuta, file.getBytes());

            // 🧠 Extraer texto del nuevo PDF
            String textoExtraido = pdfTextService.extraerTexto(file);

            // ✏️ actualizar datos
            // ✏️ actualizar datos
            doc.setNombreArchivo(nuevoNombre);
            doc.setRutaArchivo("DB_STORAGE");
            doc.setDatos(file.getBytes());
            doc.setCategoria(categoria);
            doc.setFechaSubida(LocalDate.now().toString());
            doc.setContenidoTexto(textoExtraido);

            float tamañoMB = file.getSize() / (1024f * 1024f);
            doc.setTamañoArchivo(tamañoMB);

            documentoService.guardarDocumento(doc);

            return ResponseEntity.ok(
                    Map.of("mensaje", "Documento actualizado correctamente"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Error al actualizar documento: " + e.getMessage());
        }
    }

    // =========================
    // 📊 CONTAR DOCUMENTOS HOY
    // =========================
    @GetMapping("/count/hoy")
    public ResponseEntity<Long> contarDocumentosHoy() {
        return ResponseEntity.ok(
                documentoService.contarDocumentosHoy());
    }
}