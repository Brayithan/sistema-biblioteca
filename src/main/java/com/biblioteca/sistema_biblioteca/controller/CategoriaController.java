
package com.biblioteca.sistema_biblioteca.controller;

import com.biblioteca.sistema_biblioteca.modelo.Categoria;
import com.biblioteca.sistema_biblioteca.repository.CategoriaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("categoria", new Categoria());
        return "categorias";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Categoria categoria) {
        categoriaRepository.save(categoria);
        return "redirect:/categorias";
    }
    // Editar categoría
@GetMapping("/editar/{id}")
public String editar(@PathVariable Integer id, Model model) {

    Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

    model.addAttribute("categoria", categoria);
    return "editar-categoria";
}

// Actualizar categoría
@PostMapping("/actualizar")
public String actualizar(@ModelAttribute Categoria categoria,
                          RedirectAttributes redirectAttributes) {

    categoriaRepository.save(categoria);

    redirectAttributes.addFlashAttribute(
        "mensaje", "Categoría actualizada correctamente."
    );

    return "redirect:/categorias";
}

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id,
                           RedirectAttributes redirectAttributes) {
        try {
            categoriaRepository.deleteById(id);
            redirectAttributes.addFlashAttribute(
                "mensaje", "Categoría eliminada correctamente."
            );
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute(
                "error", "No se puede eliminar esta categoría porque tiene libros asociados."
            );
        }

        return "redirect:/categorias";
    }
}
