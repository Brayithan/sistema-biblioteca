package com.biblioteca.sistema_biblioteca.repository;

import com.biblioteca.sistema_biblioteca.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para realizar operaciones CRUD sobre los libros.
 */
public interface LibroRepository extends JpaRepository<Libro, Integer> {
}