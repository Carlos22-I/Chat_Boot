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
        return registroUsuarioRepository.save(usuario);
    }
}
