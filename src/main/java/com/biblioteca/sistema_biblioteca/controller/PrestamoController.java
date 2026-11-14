package com.biblioteca.sistema_biblioteca.controller;

import com.biblioteca.sistema_biblioteca.modelo.Prestamo;
import com.biblioteca.sistema_biblioteca.repository.PrestamoRepository;
import com.biblioteca.sistema_biblioteca.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;

@Controller
@RequestMapping("/prestamos")
public class PrestamoController {

    private final PrestamoRepository prestamoRepository;
    private final UsuarioRepository usuarioRepository;

    public PrestamoController(PrestamoRepository prestamoRepository,
                              UsuarioRepository usuarioRepository) {
        this.prestamoRepository = prestamoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Listar préstamos
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("prestamos", prestamoRepository.findAll());
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("prestamo", new Prestamo());
        return "prestamos";
    }

    // Registrar préstamo
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Prestamo prestamo,
                          RedirectAttributes redirectAttributes) {

        if (prestamo.getFechaPrestamo() == null) {
            prestamo.setFechaPrestamo(LocalDate.now());
        }

        if (!usuarioRepository.existsById(prestamo.getIdUsuario())) {
            redirectAttributes.addFlashAttribute(
                "error", "El usuario seleccionado no existe."
            );
            return "redirect:/prestamos";
        }

        prestamoRepository.save(prestamo);

        redirectAttributes.addFlashAttribute(
            "mensaje", "Préstamo registrado correctamente."
        );

        return "redirect:/prestamos";
    }
    // Editar préstamo
@GetMapping("/editar/{id}")
public String editar(@PathVariable Integer id, Model model) {

    Prestamo prestamo = prestamoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

    model.addAttribute("prestamo", prestamo);
    model.addAttribute("usuarios", usuarioRepository.findAll());

    return "editar-prestamo";
}

// Actualizar préstamo
@PostMapping("/actualizar")
public String actualizar(@ModelAttribute Prestamo prestamo,
                         RedirectAttributes redirectAttributes) {

    if (!usuarioRepository.existsById(prestamo.getIdUsuario())) {
        redirectAttributes.addFlashAttribute(
                "error", "El usuario seleccionado no existe."
        );
        return "redirect:/prestamos";
    }

    prestamoRepository.save(prestamo);

    redirectAttributes.addFlashAttribute(
            "mensaje", "Préstamo actualizado correctamente."
    );

    return "redirect:/prestamos";
}

    // Eliminar préstamo
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id,
                           RedirectAttributes redirectAttributes) {
        try {
            prestamoRepository.deleteById(id);

            redirectAttributes.addFlashAttribute(
                "mensaje", "Préstamo eliminado correctamente."
            );

        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute(
                "error", "No se puede eliminar el préstamo porque tiene detalles asociados."
            );
        }

        return "redirect:/prestamos";
    }
}