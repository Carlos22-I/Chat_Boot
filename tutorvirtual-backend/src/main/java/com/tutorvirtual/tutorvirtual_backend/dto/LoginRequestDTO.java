package com.tutorvirtual.tutorvirtual_backend.dto;

public class LoginRequestDTO {

    private String email;  //  CAMBIO
    private String contraseña;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { 
        this.contraseña = contraseña; 
    }
}
