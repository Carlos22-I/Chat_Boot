package com.tutorvirtual.tutorvirtual_backend.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tutorvirtual.tutorvirtual_backend.dto.LoginRequestDTO;
import com.tutorvirtual.tutorvirtual_backend.dto.LoginResponseDTO;
import com.tutorvirtual.tutorvirtual_backend.entity.Usuario;
import com.tutorvirtual.tutorvirtual_backend.security.JwtUtil;
import com.tutorvirtual.tutorvirtual_backend.service.user.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    // ✅ UN SOLO CONSTRUCTOR
    public UsuarioController(UsuarioService usuarioService, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {

        System.out.println("👉 Email recibido: " + request.getEmail());
        System.out.println("👉 Contraseña recibida: [PROTECTED]");

        Usuario user = usuarioService.login(
                request.getEmail(),
                request.getContrasena());

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Email o contraseña incorrectos"));
        }

        String token = jwtUtil.generateToken(user.getEmail());
        LoginResponseDTO response = new LoginResponseDTO(token, user);

        return ResponseEntity.ok(response);
    }

}
