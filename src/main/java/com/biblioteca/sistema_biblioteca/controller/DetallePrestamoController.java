package com.biblioteca.sistema_biblioteca.controller;

import com.biblioteca.sistema_biblioteca.modelo.DetallePrestamo;
import com.biblioteca.sistema_biblioteca.repository.DetallePrestamoRepository;
import com.biblioteca.sistema_biblioteca.repository.PrestamoRepository;
import com.biblioteca.sistema_biblioteca.repository.LibroRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;

@Controller
@RequestMapping("/detalles-prestamo")
public class DetallePrestamoController {

    private final DetallePrestamoRepository detalleRepository;
    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;

    public DetallePrestamoController(
            DetallePrestamoRepository detalleRepository,
            PrestamoRepository prestamoRepository,
            LibroRepository libroRepository) {

        this.detalleRepository = detalleRepository;
        this.prestamoRepository = prestamoRepository;
        this.libroRepository = libroRepository;
    }

    // Listar detalles de préstamos
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("detalles", detalleRepository.findAll());
        model.addAttribute("detalle", new DetallePrestamo());
        model.addAttribute("prestamos", prestamoRepository.findAll());
        model.addAttribute("libros", libroRepository.findAll());

        return "detalles-prestamo";
    }

    // Registrar detalle de préstamo
    @PostMapping("/guardar")
public String guardar(
        @ModelAttribute DetallePrestamo detalle,
        RedirectAttributes redirectAttributes) {

    if (!prestamoRepository.existsById(detalle.getIdPrestamo())) {
        redirectAttributes.addFlashAttribute(
            "error", "El préstamo seleccionado no existe."
        );
        return "redirect:/detalles-prestamo";
    }

    if (!libroRepository.existsById(detalle.getIdLibro())) {
        redirectAttributes.addFlashAttribute(
            "error", "El libro seleccionado no existe."
        );
        return "redirect:/detalles-prestamo";
    }

    if (detalleRepository.existsByIdPrestamoAndIdLibro(
            detalle.getIdPrestamo(), detalle.getIdLibro())) {

        redirectAttributes.addFlashAttribute(
            "error", "Este libro ya está asociado a ese préstamo."
        );
        return "redirect:/detalles-prestamo";
    }

    detalleRepository.save(detalle);

    redirectAttributes.addFlashAttribute(
        "mensaje", "Libro asociado al préstamo correctamente."
    );

    return "redirect:/detalles-prestamo";
}
// Editar detalle de préstamo
@GetMapping("/editar/{id}")
public String editar(@PathVariable Integer id, Model model) {

    DetallePrestamo detalle = detalleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));

    model.addAttribute("detalle", detalle);
    model.addAttribute("prestamos", prestamoRepository.findAll());
    model.addAttribute("libros", libroRepository.findAll());

    return "editar-detalle-prestamo";
}

// Actualizar detalle de préstamo
@PostMapping("/actualizar")
public String actualizar(@ModelAttribute DetallePrestamo detalle,
                         RedirectAttributes redirectAttributes) {

    if (!prestamoRepository.existsById(detalle.getIdPrestamo())) {
        redirectAttributes.addFlashAttribute(
                "error", "El préstamo seleccionado no existe."
        );
        return "redirect:/detalles-prestamo";
    }

    if (!libroRepository.existsById(detalle.getIdLibro())) {
        redirectAttributes.addFlashAttribute(
                "error", "El libro seleccionado no existe."
        );
        return "redirect:/detalles-prestamo";
    }

    boolean duplicado = detalleRepository
            .existsByIdPrestamoAndIdLibroAndIdDetalleNot(
                    detalle.getIdPrestamo(),
                    detalle.getIdLibro(),
                    detalle.getIdDetalle()
            );

    if (duplicado) {
        redirectAttributes.addFlashAttribute(
                "error", "Este libro ya está asociado a ese préstamo."
        );
        return "redirect:/detalles-prestamo";
    }

    detalleRepository.save(detalle);

    redirectAttributes.addFlashAttribute(
            "mensaje", "Detalle de préstamo actualizado correctamente."
    );

    return "redirect:/detalles-prestamo";
}

    // Eliminar detalle de préstamo
    @GetMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes) {

        try {
            detalleRepository.deleteById(id);

            redirectAttributes.addFlashAttribute(
                "mensaje", "Detalle eliminado correctamente."
            );

        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute(
                "error", "No se pudo eliminar el detalle."
            );
        }

        return "redirect:/detalles-prestamo";
    }
}