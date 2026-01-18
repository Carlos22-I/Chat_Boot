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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tutorvirtual.tutorvirtual_backend.service.chat.ChatService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/preguntar")
    public ResponseEntity<?> preguntar(@RequestBody Map<String, String> body) {
        System.out.println("📩 Nueva pregunta recibida en el ChatController: " + body.get("pregunta"));
        String pregunta = body.get("pregunta");
        String respuesta = chatService.responderPregunta(pregunta);

        if (respuesta == null || respuesta.isBlank()) {
            return ResponseEntity.ok(
                    Map.of("respuesta", "No se pudo generar una respuesta en este momento."));
        }

        return ResponseEntity.ok(
                Map.of("respuesta", respuesta));
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
                    anchoTexto, margenInferior);

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
                    400, margenInferior);

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
            System.out.println("📥 Generando PDF Profesional...");

            Object mensajesObj = body.get("mensajes");
            if (mensajesObj == null)
                return ResponseEntity.badRequest().build();

            @SuppressWarnings("unchecked")
            List<Map<String, String>> mensajes = (List<Map<String, String>>) mensajesObj;
            if (mensajes.isEmpty())
                return ResponseEntity.badRequest().build();

            documento = new PDDocument();

            // Margenes y constantes de diseño
            float margen = 60;
            float margenInferior = 60;
            float anchoPagina = PDRectangle.A4.getWidth();
            float yPos = 750;

            PDPage paginaActual = new PDPage(PDRectangle.A4);
            documento.addPage(paginaActual);
            PDPageContentStream contenido = new PDPageContentStream(documento, paginaActual);

            // 🎨 DIBUJAR ENCABEZADO INICIAL
            yPos = dibujarEncabezado(contenido, anchoPagina, margen, yPos);

            // ========== ITERAR SOBRE MENSAJES ==========
            int numeroConsulta = 1;
            for (Map<String, String> mensaje : mensajes) {
                String tipo = mensaje.get("tipo");
                String texto = mensaje.get("texto");

                if (tipo == null || texto == null)
                    continue;

                texto = limpiarTexto(texto);

                if (tipo.equals("consulta")) {
                    if (yPos < margenInferior + 50) {
                        contenido = finalizarPaginaYCrearNueva(documento, contenido, anchoPagina, margen);
                        yPos = 730;
                    }

                    contenido.setNonStrokingColor(new Color(110, 110, 110));
                    contenido.setFont(PDType1Font.HELVETICA_BOLD_OBLIQUE, 8);
                    contenido.beginText();
                    contenido.newLineAtOffset(anchoPagina - margen - 80, yPos);
                    contenido.showText("TU CONSULTA " + numeroConsulta);
                    contenido.endText();

                    yPos -= 15;
                    contenido.setNonStrokingColor(Color.BLACK);
                    contenido.setFont(PDType1Font.HELVETICA, 10);

                    ResultadoEscritura res = escribirTextoConPaginas(documento, contenido, texto,
                            anchoPagina - margen - 300, yPos, 300, margenInferior);
                    contenido = res.contenido;
                    yPos = res.yPos - 20;

                } else if (tipo.equals("respuesta")) {
                    if (yPos < margenInferior + 70) {
                        contenido = finalizarPaginaYCrearNueva(documento, contenido, anchoPagina, margen);
                        yPos = 730;
                    }

                    contenido.setNonStrokingColor(new Color(25, 118, 210));
                    contenido.setFont(PDType1Font.HELVETICA_BOLD, 9);
                    contenido.beginText();
                    contenido.newLineAtOffset(margen, yPos);
                    contenido.showText("ASISTENTE UNAMBA");
                    contenido.endText();

                    yPos -= 15;
                    contenido.setNonStrokingColor(Color.BLACK);

                    ResultadoEscritura res = escribirTextoConPaginas(documento, contenido, texto, margen, yPos, 450,
                            margenInferior);
                    contenido = res.contenido;
                    yPos = res.yPos - 30;

                    // Línea divisoria horizontal
                    if (yPos > margenInferior) {
                        contenido.setStrokingColor(new Color(230, 230, 230));
                        contenido.setLineWidth(0.5f);
                        contenido.moveTo(margen, yPos + 10);
                        contenido.lineTo(anchoPagina - margen, yPos + 10);
                        contenido.stroke();
                    }

                    numeroConsulta++;
                }
            }

            contenido.close();
            agregarNumeracionPaginas(documento, anchoPagina, margen);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            documento.save(baos);
            byte[] pdfBytes = baos.toByteArray();
            documento.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.add("Content-Disposition", "attachment; filename=conversacion-unamba.pdf");

            return ResponseEntity.ok().headers(headers).body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    private float dibujarEncabezado(PDPageContentStream contenido, float ancho, float margen, float y)
            throws Exception {
        contenido.setNonStrokingColor(new Color(13, 71, 161));
        contenido.addRect(0, y - 20, ancho, 80);
        contenido.fill();

        contenido.setNonStrokingColor(Color.WHITE);
        contenido.setFont(PDType1Font.HELVETICA_BOLD, 22);
        contenido.beginText();
        contenido.newLineAtOffset(margen, y + 20);
        contenido.showText("UNAMBA");
        contenido.endText();

        contenido.setFont(PDType1Font.HELVETICA, 12);
        contenido.beginText();
        contenido.newLineAtOffset(margen, y);
        contenido.showText("Asistente Virtual de Tramites Documentarios");
        contenido.endText();

        return y - 60;
    }

    private PDPageContentStream finalizarPaginaYCrearNueva(PDDocument doc, PDPageContentStream actual, float ancho,
            float margen) throws Exception {
        actual.close();
        PDPage nueva = new PDPage(PDRectangle.A4);
        doc.addPage(nueva);
        PDPageContentStream nuevoContenido = new PDPageContentStream(doc, nueva);

        nuevoContenido.setNonStrokingColor(new Color(13, 71, 161));
        nuevoContenido.addRect(0, 810, ancho, 30);
        nuevoContenido.fill();

        return nuevoContenido;
    }

    private void agregarNumeracionPaginas(PDDocument doc, float ancho, float margen) throws Exception {
        int total = doc.getNumberOfPages();
        for (int i = 0; i < total; i++) {
            PDPage page = doc.getPage(i);
            PDPageContentStream footer = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true,
                    true);

            footer.setNonStrokingColor(new Color(150, 150, 150));
            footer.setFont(PDType1Font.HELVETICA, 8);
            footer.beginText();
            String info = "UNAMBA - " + java.time.LocalDate.now() + "  |  Pagina " + (i + 1) + " de " + total;
            footer.newLineAtOffset(ancho / 2 - 80, 30);
            footer.showText(info);
            footer.endText();
            footer.close();
        }
    }

    private String limpiarTexto(String texto) {
        if (texto == null)
            return "";

        return texto
                .replaceAll("\\*\\*", "")
                .replaceAll("\\r", "")
                .replaceAll("(?m)^\\* ", "• ")
                .replaceAll("[\\x{1F300}-\\x{1F9FF}]", "")
                .replaceAll("[\\x{2600}-\\x{26FF}]", "")
                .replaceAll("[\\x{2700}-\\x{27BF}]", "")
                .replaceAll("[\\x{1F600}-\\x{1F64F}]", "")
                .replaceAll("[\\x{1F680}-\\x{1F6FF}]", "")
                .replaceAll("[\\x{1F900}-\\x{1F9FF}]", "")
                .replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u")
                .replace("Á", "A").replace("É", "E").replace("Í", "I").replace("Ó", "O").replace("Ú", "U")
                .replace("ñ", "n").replace("Ñ", "N")
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
            float margenInferior) throws Exception {

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
            int maxChars) throws Exception {

        String[] palabras = texto.split("\\s+");
        StringBuilder linea = new StringBuilder();
        float yActual = y;

        contenido.beginText();
        contenido.newLineAtOffset(x, yActual);

        for (String palabra : palabras) {
            if (palabra.trim().isEmpty())
                continue;

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