package com.tutorvirtual.tutorvirtual_backend.service.chat;

import org.springframework.stereotype.Service;

import com.tutorvirtual.tutorvirtual_backend.service.gemini.GeminiService;



@Service
public class ChatService {

    private final GeminiService geminiService;

    public ChatService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    public String responderPregunta(String pregunta) {

    System.out.println("PREGUNTA RECIBIDA: " + pregunta);

    String respuesta = geminiService.generarRespuesta(pregunta);

    System.out.println("RESPUESTA DE GEMINI: " + respuesta);

    return respuesta;
}

}


