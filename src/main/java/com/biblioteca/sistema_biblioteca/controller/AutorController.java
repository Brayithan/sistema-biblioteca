package com.biblioteca.sistema_biblioteca.controller;

import com.biblioteca.sistema_biblioteca.modelo.Autor;
import com.biblioteca.sistema_biblioteca.repository.AutorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/autores")
public class AutorController {

    private final AutorRepository autorRepository;

    public AutorController(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    // Listar autores registrados
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("autores", autorRepository.findAll());
        model.addAttribute("autor", new Autor());
        return "autores";
    }

    // Registrar un nuevo autor
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Autor autor) {
        autorRepository.save(autor);
        return "redirect:/autores";
    }
    // Editar autor
@GetMapping("/editar/{id}")
public String editar(@PathVariable Integer id, Model model) {

    Autor autor = autorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Autor no encontrado"));

    model.addAttribute("autor", autor);
    return "editar-autor";
}
// Actualizar autor
@PostMapping("/actualizar")
public String actualizar(@ModelAttribute Autor autor,
                         RedirectAttributes redirectAttributes) {

    autorRepository.save(autor);

    redirectAttributes.addFlashAttribute(
        "mensaje", "Autor actualizado correctamente."
    );

    return "redirect:/autores";
}

    // Eliminar un autor y controlar las relaciones con libros
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id,
                           RedirectAttributes redirectAttributes) {

        try {
            autorRepository.deleteById(id);

            redirectAttributes.addFlashAttribute(
                "mensaje",
                "Autor eliminado correctamente."
            );

        } catch (DataIntegrityViolationException e) {

            redirectAttributes.addFlashAttribute(
                "error",
                "No se puede eliminar este autor porque tiene libros asociados."
            );
        }

        return "redirect:/autores";
    }
}