package com.tutorvirtual.tutorvirtual_backend.controller;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tutorvirtual.tutorvirtual_backend.service.chat.ChatService;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/preguntar")
    public ResponseEntity<?> preguntar(@RequestBody Map<String, String> body) {
        String pregunta = body.get("pregunta");
        String respuesta = chatService.responderPregunta(pregunta);

        if (respuesta == null || respuesta.isBlank()) {
            return ResponseEntity.ok(
                Map.of("respuesta", "No se pudo generar una respuesta en este momento.")
            );
        }

        return ResponseEntity.ok(
            Map.of("respuesta", respuesta)
        );
    }

    // Endpoint ANTIGUO: descargar solo última pregunta/respuesta
    @PostMapping("/descargar-respuesta")
    public ResponseEntity<byte[]> descargarRespuesta(@RequestBody Map<String, String> body) {
        
        PDDocument documento = null;
        
        try {
            String pregunta = body.get("pregunta");
            String respuesta = body.get("respuesta");

            if (pregunta == null || respuesta == null) {
                return ResponseEntity.badRequest().build();
            }

            System.out.println("📄 Generando PDF simple (1 pregunta/respuesta)...");

            String preguntaLimpia = limpiarTexto(pregunta);
            String respuestaLimpia = limpiarTexto(respuesta);

            documento = new PDDocument();
            
            PDPage paginaActual = new PDPage(PDRectangle.A4);
            documento.addPage(paginaActual);
            PDPageContentStream contenido = new PDPageContentStream(documento, paginaActual);
            
            float margen = 50;
            float yPos = 750;
            float anchoPagina = PDRectangle.A4.getWidth();
            float margenInferior = 50;

            // Encabezado
            contenido.setNonStrokingColor(new Color(25, 118, 210));
            contenido.addRect(margen - 10, yPos - 5, anchoPagina - 2 * margen + 20, 40);
            contenido.fill();

            contenido.setNonStrokingColor(Color.WHITE);
            contenido.setFont(PDType1Font.HELVETICA_BOLD, 20);
            contenido.beginText();
            contenido.newLineAtOffset(margen + 10, yPos + 10);
            contenido.showText("UNAMBA - Asistente de Tramites");
            contenido.endText();

            contenido.setNonStrokingColor(Color.BLACK);
            yPos -= 70;

            // Consulta (derecha)
            float anchoTexto = 350;
            float xConsulta = anchoPagina - margen - anchoTexto;
            
            contenido.setFont(PDType1Font.HELVETICA_BOLD, 9);
            contenido.beginText();
            contenido.newLineAtOffset(xConsulta, yPos);
            contenido.showText("TU");
            contenido.endText();

            yPos -= 18;
            
            contenido.setFont(PDType1Font.HELVETICA, 10);
            ResultadoEscritura resultadoConsulta = escribirTextoConPaginas(
                documento, contenido, preguntaLimpia, xConsulta, yPos, 
                anchoTexto, margenInferior
            );
            
            contenido = resultadoConsulta.contenido;
            yPos = resultadoConsulta.yPos - 15;

            // Respuesta (izquierda)
            float xRespuesta = margen;
            
            if (yPos < margenInferior + 60) {
                contenido.close();
                paginaActual = new PDPage(PDRectangle.A4);
                documento.addPage(paginaActual);
                contenido = new PDPageContentStream(documento, paginaActual);
                yPos = 750;
            }
            
            contenido.setFont(PDType1Font.HELVETICA_BOLD, 9);
            contenido.beginText();
            contenido.newLineAtOffset(xRespuesta, yPos);
            contenido.showText("ASISTENTE UNAMBA");
            contenido.endText();

            yPos -= 18;

            ResultadoEscritura resultado = escribirTextoConPaginas(
                documento, contenido, respuestaLimpia, xRespuesta, yPos, 
                400, margenInferior
            );
            
            contenido = resultado.contenido;
            contenido.close();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            documento.save(baos);
            byte[] pdfBytes = baos.toByteArray();
            
            documento.close();

            System.out.println("✅ PDF generado: " + pdfBytes.length + " bytes");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.add("Content-Disposition", "attachment; filename=respuesta-unamba.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            System.err.println("❌ ERROR generando PDF:");
            e.printStackTrace();
            
            if (documento != null) {
                try {
                    documento.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            
            return ResponseEntity.internalServerError().build();
        }
    }

    // Endpoint NUEVO: descargar conversación completa
    @PostMapping("/descargar-conversacion")
    public ResponseEntity<byte[]> descargarConversacion(@RequestBody Map<String, Object> body) {
        
        PDDocument documento = null;
        
        try {
            System.out.println("📥 Recibiendo solicitud de conversación...");
            System.out.println("📦 Body recibido: " + body);
            
            // Recibir array completo de mensajes
            Object mensajesObj = body.get("mensajes");
            if (mensajesObj == null) {
                System.err.println("❌ No se encontró el campo 'mensajes' en el body");
                return ResponseEntity.badRequest().build();
            }
            
            List<Map<String, String>> mensajes = (List<Map<String, String>>) mensajesObj;

            if (mensajes.isEmpty()) {
                System.err.println("❌ La lista de mensajes está vacía");
                return ResponseEntity.badRequest().build();
            }

            System.out.println("📄 Generando PDF con " + mensajes.size() + " mensajes...");
            
            // Validar estructura de mensajes
            for (int i = 0; i < mensajes.size(); i++) {
                Map<String, String> msg = mensajes.get(i);
                System.out.println("Mensaje " + i + ": tipo=" + msg.get("tipo") + ", texto length=" + 
                    (msg.get("texto") != null ? msg.get("texto").length() : "null"));
            }

            documento = new PDDocument();
            
            PDPage paginaActual = new PDPage(PDRectangle.A4);
            documento.addPage(paginaActual);
            PDPageContentStream contenido = new PDPageContentStream(documento, paginaActual);
            
            float margen = 50;
            float yPos = 750;
            float anchoPagina = PDRectangle.A4.getWidth();
            float margenInferior = 50;

            // ========== ENCABEZADO ==========
            contenido.setNonStrokingColor(new Color(25, 118, 210));
            contenido.addRect(margen - 10, yPos - 5, anchoPagina - 2 * margen + 20, 40);
            contenido.fill();

            contenido.setNonStrokingColor(Color.WHITE);
            contenido.setFont(PDType1Font.HELVETICA_BOLD, 20);
            contenido.beginText();
            contenido.newLineAtOffset(margen + 10, yPos + 10);
            contenido.showText("UNAMBA - Asistente de Tramites");
            contenido.endText();

            contenido.setNonStrokingColor(Color.BLACK);
            yPos -= 70;

            // ========== ITERAR SOBRE TODOS LOS MENSAJES ==========
            int numeroMensaje = 1;
            
            for (Map<String, String> mensaje : mensajes) {
                String tipo = mensaje.get("tipo");
                String texto = mensaje.get("texto");
                
                if (tipo == null || texto == null) {
                    System.err.println("⚠️ Mensaje con campos nulos: tipo=" + tipo + ", texto=" + texto);
                    continue;
                }
                
                texto = limpiarTexto(texto);
                
                System.out.println("🔄 Procesando " + tipo + " #" + numeroMensaje + " (length: " + texto.length() + ")");

                if (tipo.equals("consulta")) {
                    // ===== CONSULTA (DERECHA sin cuadro, solo texto) =====
                    float anchoTexto = 350;
                    float xConsulta = anchoPagina - margen - anchoTexto;
                    
                    // Verificar espacio
                    if (yPos < margenInferior + 60) {
                        contenido.close();
                        paginaActual = new PDPage(PDRectangle.A4);
                        documento.addPage(paginaActual);
                        contenido = new PDPageContentStream(documento, paginaActual);
                        yPos = 750;
                    }

                    // Etiqueta "CONSULTA X" alineada a la derecha
                    contenido.setNonStrokingColor(Color.BLACK);
                    contenido.setFont(PDType1Font.HELVETICA_BOLD, 9);
                    contenido.beginText();
                    contenido.newLineAtOffset(xConsulta, yPos);
                    contenido.showText("CONSULTA " + numeroMensaje);
                    contenido.endText();

                    yPos -= 18;

                    // Texto sin cuadro
                    contenido.setFont(PDType1Font.HELVETICA, 10);
                    ResultadoEscritura resultadoConsulta = escribirTextoConPaginas(
                        documento, contenido, texto, xConsulta, yPos, 
                        anchoTexto, margenInferior
                    );
                    
                    contenido = resultadoConsulta.contenido;
                    yPos = resultadoConsulta.yPos - 15;

                } else if (tipo.equals("respuesta")) {
                    // ===== RESPUESTA (IZQUIERDA sin cuadro) =====
                    float xRespuesta = margen;
                    float anchoRespuesta = 400;
                    
                    // Verificar espacio
                    if (yPos < margenInferior + 60) {
                        contenido.close();
                        paginaActual = new PDPage(PDRectangle.A4);
                        documento.addPage(paginaActual);
                        contenido = new PDPageContentStream(documento, paginaActual);
                        yPos = 750;
                    }

                    // Etiqueta
                    contenido.setFont(PDType1Font.HELVETICA_BOLD, 8);
                    contenido.beginText();
                    contenido.newLineAtOffset(xRespuesta, yPos);
                    contenido.showText("RESPUESTA " + numeroMensaje);
                    contenido.endText();

                    yPos -= 18;

                    // Escribir respuesta con manejo de páginas
                    ResultadoEscritura resultado = escribirTextoConPaginas(
                        documento, contenido, texto, xRespuesta, yPos, 
                        anchoRespuesta, margenInferior
                    );
                    
                    contenido = resultado.contenido;
                    yPos = resultado.yPos - 20;
                    
                    numeroMensaje++;
                    
                    // ===== LÍNEA SEPARADORA después de cada par consulta-respuesta =====
                    if (yPos < margenInferior + 40) {
                        contenido.close();
                        paginaActual = new PDPage(PDRectangle.A4);
                        documento.addPage(paginaActual);
                        contenido = new PDPageContentStream(documento, paginaActual);
                        yPos = 750;
                    }
                    
                    contenido.setStrokingColor(new Color(200, 200, 200));
                    contenido.setLineWidth(0.5f);
                    contenido.moveTo(margen, yPos);
                    contenido.lineTo(anchoPagina - margen, yPos);
                    contenido.stroke();
                    
                    yPos -= 25;
                }
            }

            contenido.close();

            int totalPaginas = documento.getNumberOfPages();
            System.out.println("📄 Total de páginas: " + totalPaginas);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            documento.save(baos);
            byte[] pdfBytes = baos.toByteArray();
            
            documento.close();

            System.out.println("✅ PDF generado: " + pdfBytes.length + " bytes");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.add("Content-Disposition", "attachment; filename=conversacion-unamba.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            System.err.println("❌ ERROR generando PDF:");
            e.printStackTrace();
            
            if (documento != null) {
                try {
                    documento.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            
            return ResponseEntity.internalServerError().build();
        }
    }

    private String limpiarTexto(String texto) {
        if (texto == null) return "";
        
        return texto
            .replaceAll("\\*\\*", "")  // Eliminar negritas markdown
            .replaceAll("\\r", "")     // Eliminar retornos de carro
            .replaceAll("={3,}", "")   // Eliminar ===
            .replaceAll("-{3,}", "")   // Eliminar ---
            // Eliminar TODOS los emojis (rangos Unicode de emojis)
            .replaceAll("[\\p{So}\\p{Sc}\\p{Sk}\\p{Sm}]", "")
            .replaceAll("[\\x{1F300}-\\x{1F9FF}]", "") // Emojis y símbolos
            .replaceAll("[\\x{2600}-\\x{26FF}]", "")   // Símbolos varios
            .replaceAll("[\\x{2700}-\\x{27BF}]", "")   // Dingbats
            .replaceAll("[\\x{1F600}-\\x{1F64F}]", "") // Emoticones
            .replaceAll("[\\x{1F680}-\\x{1F6FF}]", "") // Transporte y símbolos de mapa
            .replaceAll("[\\x{1F900}-\\x{1F9FF}]", "") // Símbolos suplementarios
            .replaceAll("[\\x{2000}-\\x{206F}]", "")   // Puntuación general
            .trim();
    }

    private float calcularAlturaTexto(String texto, int maxChars) {
        String[] lineas = texto.split("\\n+");
        int totalLineas = 0;
        
        for (String linea : lineas) {
            if (linea.trim().isEmpty()) {
                totalLineas++;
                continue;
            }
            int lineasEstimadas = (linea.length() / maxChars) + 1;
            totalLineas += lineasEstimadas;
        }
        
        return Math.max(totalLineas * 12, 30);
    }

    private ResultadoEscritura escribirTextoConPaginas(
        PDDocument documento,
        PDPageContentStream contenido,
        String texto,
        float x,
        float yInicial,
        float anchoMax,
        float margenInferior
    ) throws Exception {
        
        float yActual = yInicial;
        float alturaLinea = 13;
        
        String[] lineas = texto.split("\\n+");

        for (String linea : lineas) {
            linea = linea.trim();
            
            if (linea.isEmpty()) {
                yActual -= alturaLinea / 2;
                continue;
            }

            boolean esTitulo = linea.matches(".*(PASO\\s*\\d+:|REQUISITOS|PROCEDIMIENTO|IMPORTANTE|NOTA).*");
            
            // Verificar si necesita nueva página
            if (yActual < margenInferior + 40) {
                System.out.println("📄 Creando nueva página para respuesta");
                
                contenido.close();
                
                PDPage nuevaPagina = new PDPage(PDRectangle.A4);
                documento.addPage(nuevaPagina);
                contenido = new PDPageContentStream(documento, nuevaPagina);
                
                yActual = 750;
            }

            if (esTitulo) {
                yActual -= 4;
                contenido.setFont(PDType1Font.HELVETICA_BOLD, 10);
            } else {
                contenido.setFont(PDType1Font.HELVETICA, 9);
            }

            yActual = escribirLineaConSaltos(contenido, linea, x, yActual, alturaLinea, 65);
            
            if (esTitulo) {
                yActual -= 4;
            }
        }

        return new ResultadoEscritura(contenido, yActual);
    }

    private float escribirLineaConSaltos(
        PDPageContentStream contenido,
        String texto,
        float x,
        float y,
        float alturaLinea,
        int maxChars
    ) throws Exception {
        
        String[] palabras = texto.split("\\s+");
        StringBuilder linea = new StringBuilder();
        float yActual = y;

        contenido.beginText();
        contenido.newLineAtOffset(x, yActual);

        for (String palabra : palabras) {
            if (palabra.trim().isEmpty()) continue;

            String lineaPrueba = linea.length() > 0 
                ? linea + " " + palabra 
                : palabra;

            if (lineaPrueba.length() > maxChars) {
                if (linea.length() > 0) {
                    contenido.showText(linea.toString());
                    contenido.newLineAtOffset(0, -alturaLinea);
                    yActual -= alturaLinea;
                }
                linea = new StringBuilder(palabra);
            } else {
                linea = new StringBuilder(lineaPrueba);
            }
        }

        if (linea.length() > 0) {
            contenido.showText(linea.toString());
            yActual -= alturaLinea;
        }

        contenido.endText();
        
        return yActual;
    }

    private static class ResultadoEscritura {
        PDPageContentStream contenido;
        float yPos;

        ResultadoEscritura(PDPageContentStream contenido, float yPos) {
            this.contenido = contenido;
            this.yPos = yPos;
        }
    }
}