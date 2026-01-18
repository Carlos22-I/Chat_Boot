package com.tutorvirtual.tutorvirtual_backend.service.gemini;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
        try {
            // ✅ OPTIMIZADO: Solo traemos Nombre y Texto (evita cargar bytes)
            List<Object[]> documentos = documentoService.listarParaGemini();

            StringBuilder contexto = new StringBuilder();
            contexto.append("Base de conocimientos de trámites UNAMBA:\n\n");

            if (documentos == null || documentos.isEmpty()) {
                System.out.println("⚠️ No hay documentos cargados en la base de datos.");
            } else {
                for (Object[] row : documentos) {
                    String nombre = (String) row[0];
                    String texto = (String) row[1];

                    if (texto != null && !texto.isBlank()) {
                        contexto.append("📄 Fuente: ").append(nombre).append("\n");
                        // Limitar texto por documento para no exceder tokens básicos
                        if (texto.length() > 2000) {
                            contexto.append(texto.substring(0, 2000)).append("...\n\n");
                        } else {
                            contexto.append(texto).append("\n\n");
                        }
                    }
                }
            }

            String prompt = """
                    Eres un asistente virtual de la UNAMBA llamado "Tutor Virtual".
                    Ayuda a los estudiantes con sus trámites usando emojis (😊, 📋, ✅).

                    INFORMACIÓN DISPONIBLE:
                    %s

                    PREGUNTA DEL ESTUDIANTE:
                    %s
                    """.formatted(contexto.toString(), pregunta);

            Map<String, Object> body = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(Map.of("text", prompt)))));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            String url = apiUrl + "key=" + apiKey;

            System.out.println("🚀 Consultando Gemini API...");

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);

            if (response == null || !response.containsKey("candidates")) {
                return "Lo siento, la IA no respondió. Por favor intenta de nuevo en un momento.";
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            Map<String, Object> candidate = (Map<String, Object>) candidates.get(0);
            Map<String, Object> content = (Map<String, Object>) candidate.get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");

            return parts.get(0).get("text").toString();

        } catch (Exception e) {
            System.err.println("❌ Error crítico en GeminiService: " + e.getMessage());
            e.printStackTrace();
            return "Error al conectar con el servidor de inteligencia artificial. Inténtalo más tarde.";
        }
    }
}