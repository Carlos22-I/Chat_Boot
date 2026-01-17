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
    public Usuario login(String email, String contrasena) {

        Optional<Usuario> userOptional = usuarioRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return null;
        }

        Usuario user = userOptional.get();

        if (passwordEncoder.matches(contrasena, user.getContrasena())) {
            return user;
        }

        return null;
    }

    // Registro con validación de email
    public Usuario registrar(Usuario usuario) {

        boolean correoExiste = usuarioRepository.findByEmail(usuario.getEmail()).isPresent();
        boolean usuarioExiste = usuarioRepository.findByNombreUsuario(usuario.getNombreUsuario()).isPresent();

        if (correoExiste && usuarioExiste) {
            throw new RuntimeException("El correo: " + usuario.getEmail() + " y el nombre de usuario: "
                    + usuario.getNombreUsuario() + " ya existen");
        } else if (correoExiste) {
            throw new RuntimeException("El correo: " + usuario.getEmail() + " ya existe");
        } else if (usuarioExiste) {
            throw new RuntimeException("El nombre de usuario: " + usuario.getNombreUsuario() + " ya existe");
        }

        // Validar formato de email
        if (!isValidEmail(usuario.getEmail())) {
            throw new RuntimeException("Email inválido");
        }

        // Encriptar contraseña
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));

        return usuarioRepository.save(usuario);
    }

    // Validación de formato de email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}