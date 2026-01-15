package com.tutorvirtual.tutorvirtual_backend.dto;

public class ChatResponseDTO {

    private String respuesta;

    public ChatResponseDTO(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getRespuesta() {
        return respuesta;
    }
}
