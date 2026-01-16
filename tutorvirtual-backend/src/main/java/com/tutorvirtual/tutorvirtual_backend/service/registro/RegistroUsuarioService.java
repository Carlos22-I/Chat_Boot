package com.tutorvirtual.tutorvirtual_backend.service.registro;

import com.tutorvirtual.tutorvirtual_backend.entity.RegistroUsuario;
import com.tutorvirtual.tutorvirtual_backend.repository.RegistroUsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class RegistroUsuarioService {

    private final RegistroUsuarioRepository registroUsuarioRepository;

    public RegistroUsuarioService(RegistroUsuarioRepository registroUsuarioRepository) {
        this.registroUsuarioRepository = registroUsuarioRepository;
    }

    public RegistroUsuario registrar(RegistroUsuario usuario) {
        boolean correoExiste = registroUsuarioRepository.findByCorreo(usuario.getCorreo()).isPresent();
        boolean usuarioExiste = registroUsuarioRepository.findByNombreUsuario(usuario.getNombreUsuario()).isPresent();

        if (correoExiste && usuarioExiste) {
            throw new RuntimeException("El correo: " + usuario.getCorreo() + " y el nombre de usuario: "
                    + usuario.getNombreUsuario() + " ya existen");
        } else if (correoExiste) {
            throw new RuntimeException("El correo: " + usuario.getCorreo() + " ya existe");
        } else if (usuarioExiste) {
            throw new RuntimeException("El nombre de usuario: " + usuario.getNombreUsuario() + " ya existe");
        }

        return registroUsuarioRepository.save(usuario);
    }
}
