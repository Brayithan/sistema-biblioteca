package com.biblioteca.sistema_biblioteca.controller;

import com.biblioteca.sistema_biblioteca.Libro;
import com.biblioteca.sistema_biblioteca.repository.LibroRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar los libros de la biblioteca.
 */
@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroRepository libroRepository;

    public LibroController(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    // Consultar todos los libros
    @GetMapping
    public List<Libro> listarLibros() {
        return libroRepository.findAll();
    }

    // Registrar un nuevo libro
    @PostMapping
    public Libro guardarLibro(@RequestBody Libro libro) {
        return libroRepository.save(libro);
    }

    // Buscar un libro por su ID
    @GetMapping("/{id}")
    public Libro buscarLibro(@PathVariable Integer id) {
        return libroRepository.findById(id).orElse(null);
    }

    // Actualizar un libro
    @PutMapping("/{id}")
    public Libro actualizarLibro(@PathVariable Integer id,
                                  @RequestBody Libro libro) {

        return libroRepository.findById(id)
                .map(libroExistente -> {
                    libroExistente.setTitulo(libro.getTitulo());
                    return libroRepository.save(libroExistente);
                })
                .orElse(null);
    }

    // Eliminar un libro
    @DeleteMapping("/{id}")
    public void eliminarLibro(@PathVariable Integer id) {
        libroRepository.deleteById(id);
    }
}