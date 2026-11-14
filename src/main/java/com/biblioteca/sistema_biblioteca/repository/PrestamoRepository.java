package com.biblioteca.sistema_biblioteca.repository;

import com.biblioteca.sistema_biblioteca.modelo.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {

}