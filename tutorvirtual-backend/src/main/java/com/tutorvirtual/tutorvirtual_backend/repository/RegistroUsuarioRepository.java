package com.tutorvirtual.tutorvirtual_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tutorvirtual.tutorvirtual_backend.entity.RegistroUsuario;

public interface RegistroUsuarioRepository extends JpaRepository<RegistroUsuario, Long> {
    Optional<RegistroUsuario> findByNombreUsuario(String nombreUsuario);
    Optional<RegistroUsuario> findByCorreo(String correo);
}
