package com.tutorvirtual.tutorvirtual_backend.service.gemini;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tutorvirtual.tutorvirtual_backend.entity.Documento;
import com.tutorvirtual.tutorvirtual_backend.service.documento.DocumentoService;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final DocumentoService documentoService;

    public GeminiService(DocumentoService documentoService) {
        this.documentoService = documentoService;
    }

    public String generarRespuesta(String pregunta) {

        List<Documento> documentos = documentoService.listarTodos();

        StringBuilder contexto = new StringBuilder();
        contexto.append("Base de conocimientos de trámites UNAMBA:\n\n");

        if (documentos.isEmpty()) {
            return "Lo siento, aún no tengo información cargada sobre trámites.";
        }

        for (Documento doc : documentos) {
            contexto.append("📄 Fuente: ")
                    .append(doc.getNombreArchivo())
                    .append("\n");

            String contenido = doc.getContenidoTexto();
            if (contenido != null && !contenido.isBlank()) {
                if (contenido.length() > 3000) {
                    contexto.append(contenido.substring(0, 3000))
                            .append("...\n\n");
                } else {
                    contexto.append(contenido).append("\n\n");
                }
            }
        }

        String prompt = """
                Eres un asistente virtual amigable de la UNAMBA llamado "Tutor Virtual".
                Tu objetivo es ayudar a estudiantes con sus trámites de forma cálida y profesional.

                INFORMACIÓN DISPONIBLE:
                %s

                INSTRUCCIONES:
                - Sé conversacional y amigable. Usa emojis ocasionalmente (😊, 📋, ✅, 💡)
                - Si te saludan, saluda de vuelta cálidamente
                - Si preguntan por un trámite, explica los pasos de forma clara:

                  "¡Claro! 😊 Te ayudo con [trámite]. Estos son los pasos:

                  📋 PASO 1: [Título]
                  [Descripción detallada]

                  📋 PASO 2: [Título]
                  [Descripción detallada]

                  📋 PASO 3: [Título]
                  [Descripción detallada]

                  💡 Información adicional:
                  - Horarios: [...]
                  - Costo: [...]
                  - Ubicación: [...]

                  ¿Hay algo más en lo que pueda ayudarte? 😊"

                - Si hacen preguntas de seguimiento, responde de forma natural y conversacional
                - VARÍA tus respuestas. No repitas exactamente lo mismo si preguntan dos veces
                - Si no tienes información, sé honesto y sugiere alternativas

                PREGUNTA DEL ESTUDIANTE:
                %s
                """.formatted(contexto.toString(), pregunta);

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt)))));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        String url = apiUrl + "key=" + apiKey;

        try {
            System.out.println("🚀 Enviando pregunta a Gemini...");
            System.out.println("📄 Documentos en contexto: " + documentos.size());

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);

            if (response == null || !response.containsKey("candidates")) {
                return "Error: No se recibió respuesta de Gemini.";
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            Map<String, Object> candidate = candidates.get(0);

            @SuppressWarnings("unchecked")
            Map<String, Object> content = (Map<String, Object>) candidate.get("content");

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            Map<String, Object> part = parts.get(0);

            String respuesta = part.get("text").toString();
            System.out.println("✅ Respuesta generada correctamente");

            return respuesta;

        } catch (Exception e) {
            System.err.println("❌ Error al procesar con Gemini: " + e.getMessage());
            e.printStackTrace();
            return "Error al procesar la respuesta con IA.";
        }
    }
}