package com.biblioteca.sistema_biblioteca.repository;

import com.biblioteca.sistema_biblioteca.modelo.DetallePrestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePrestamoRepository extends JpaRepository<DetallePrestamo, Integer> {

    // Verificar si un libro ya está asociado a un préstamo
    boolean existsByIdPrestamoAndIdLibro(Integer idPrestamo, Integer idLibro);

    // Verificar duplicados al editar un detalle, excluyendo el registro actual
    boolean existsByIdPrestamoAndIdLibroAndIdDetalleNot(
            Integer idPrestamo,
            Integer idLibro,
            Integer idDetalle
    );

}