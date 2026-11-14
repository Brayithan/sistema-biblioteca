package com.biblioteca.sistema_biblioteca.repository;

import com.biblioteca.sistema_biblioteca.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Comprobar si un correo ya está registrado
    boolean existsByCorreo(String correo);

    // Buscar un usuario por su correo electrónico
    Usuario findByCorreo(String correo);
}