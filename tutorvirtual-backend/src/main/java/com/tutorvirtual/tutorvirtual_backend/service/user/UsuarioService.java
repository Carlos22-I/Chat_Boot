package com.tutorvirtual.tutorvirtual_backend.service.user;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tutorvirtual.tutorvirtual_backend.entity.Usuario;
import com.tutorvirtual.tutorvirtual_backend.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Login con EMAIL
    public Usuario login(String email, String contraseña) {
        
        Optional<Usuario> userOptional = usuarioRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return null;
        }

        Usuario user = userOptional.get();

        if (passwordEncoder.matches(contraseña, user.getContraseña())) {
            return user;
        }

        return null;
    }

    //  Registro con validación de email
    public Usuario registrar(Usuario usuario) {

        // Validar email duplicado
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Validar formato de email
        if (!isValidEmail(usuario.getEmail())) {
            throw new RuntimeException("Email inválido");
        }

        
        // Encriptar contraseña
        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));

        return usuarioRepository.save(usuario);
    }

    // Validación de formato de email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}