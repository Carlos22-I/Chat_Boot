package com.tutorvirtual.tutorvirtual_backend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tutorvirtual.tutorvirtual_backend.dto.RegistroRequestDTO;
import com.tutorvirtual.tutorvirtual_backend.entity.RegistroUsuario;
import com.tutorvirtual.tutorvirtual_backend.entity.Usuario;
import com.tutorvirtual.tutorvirtual_backend.service.registro.RegistroUsuarioService;
import com.tutorvirtual.tutorvirtual_backend.service.user.UsuarioService;

@RestController
@RequestMapping("/api/usuarios/register")
public class RegistroUsuarioController {

    private final RegistroUsuarioService registroService;
    private final UsuarioService usuarioService;

    public RegistroUsuarioController(
            RegistroUsuarioService registroService,
            UsuarioService usuarioService) {
        this.registroService = registroService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody RegistroRequestDTO dto) {

        System.out.println("📥 Datos recibidos:");
        System.out.println("Nombres: " + dto.getApellidosNombres());
        System.out.println("Usuario: " + dto.getNombreUsuario());
        System.out.println("Email: " + dto.getCorreo());
        System.out.println("Contraseña: [PROTECTED]");

        // 1️⃣ Validar contraseñas
        if (!dto.getContrasena().equals(dto.getConfirmarContrasena())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Las contraseñas no coinciden"));
        }

        // 2️⃣ Validar formato de email
        if (!isValidEmail(dto.getCorreo())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email inválido"));
        }

        try {
            // 3️⃣ Guardar en registro_usuarios
            RegistroUsuario registro = new RegistroUsuario();
            registro.setApellidosNombres(dto.getApellidosNombres());
            registro.setNombreUsuario(dto.getNombreUsuario());
            registro.setCorreo(dto.getCorreo());
            registro.setContrasena(dto.getContrasena());

            RegistroUsuario registroGuardado = registroService.registrar(registro);
            System.out.println("✅ Guardado en registro_usuarios: " + registroGuardado.getId());

            // 4️⃣ Crear usuario para login
            Usuario usuario = new Usuario();
            usuario.setEmail(dto.getCorreo()); // ✅ Email
            usuario.setNombreUsuario(dto.getNombreUsuario()); // Display name
            usuario.setContrasena(dto.getContrasena());

            Usuario usuarioGuardado = usuarioService.registrar(usuario);
            System.out.println("✅ Guardado en usuarios: " + usuarioGuardado.getId());

            return ResponseEntity.ok(
                    Map.of("mensaje", "resgistro existoso"));

        } catch (RuntimeException e) {
            System.err.println("❌ Error: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Validación de email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}
