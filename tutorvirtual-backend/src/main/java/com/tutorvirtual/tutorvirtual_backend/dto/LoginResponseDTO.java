package com.tutorvirtual.tutorvirtual_backend.dto;

import com.tutorvirtual.tutorvirtual_backend.entity.Usuario;

public class LoginResponseDTO {

    private String token;
    private Usuario usuario;

    public LoginResponseDTO(String token, Usuario usuario) {
        this.token = token;
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
