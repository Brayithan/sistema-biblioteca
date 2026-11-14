package com.biblioteca.sistema_biblioteca.controller;

import com.biblioteca.sistema_biblioteca.modelo.Usuario;
import com.biblioteca.sistema_biblioteca.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository,
                             PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Listar usuarios
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("usuario", new Usuario());
        return "usuarios";
    }

    // Registrar usuarios con contraseña cifrada
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Usuario usuario,
                          RedirectAttributes redirectAttributes) {

        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            redirectAttributes.addFlashAttribute(
                "error", "Este correo ya se encuentra registrado."
            );
            return "redirect:/usuarios";
        }

        usuario.setContrasena(
            passwordEncoder.encode(usuario.getContrasena())
        );

        usuarioRepository.save(usuario);

        redirectAttributes.addFlashAttribute(
            "mensaje", "Usuario registrado correctamente."
        );

        return "redirect:/usuarios";
    }
    // Editar usuario
@GetMapping("/editar/{id}")
public String editar(@PathVariable Integer id, Model model) {

    Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    model.addAttribute("usuario", usuario);
    return "editar-usuario";
}

// Actualizar usuario
@PostMapping("/actualizar")
public String actualizar(@ModelAttribute Usuario usuario,
                         RedirectAttributes redirectAttributes) {

    Usuario usuarioExistente = usuarioRepository.findById(usuario.getIdUsuario())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    usuarioExistente.setNombre(usuario.getNombre());
    usuarioExistente.setCorreo(usuario.getCorreo());
    usuarioExistente.setTelefono(usuario.getTelefono());
    usuarioExistente.setDireccion(usuario.getDireccion());

    usuarioRepository.save(usuarioExistente);

    redirectAttributes.addFlashAttribute(
        "mensaje", "Usuario actualizado correctamente."
    );

    return "redirect:/usuarios";
}

    // Eliminar usuarios
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id,
                           RedirectAttributes redirectAttributes) {

        try {
            usuarioRepository.deleteById(id);

            redirectAttributes.addFlashAttribute(
                "mensaje", "Usuario eliminado correctamente."
            );

        } catch (DataIntegrityViolationException e) {

            redirectAttributes.addFlashAttribute(
                "error", "No se puede eliminar este usuario porque tiene préstamos asociados."
            );
        }

        return "redirect:/usuarios";
    }
}