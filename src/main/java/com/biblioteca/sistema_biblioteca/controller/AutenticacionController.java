package com.biblioteca.sistema_biblioteca.controller;

import com.biblioteca.sistema_biblioteca.modelo.Usuario;
import com.biblioteca.sistema_biblioteca.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AutenticacionController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AutenticacionController(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Mostrar inicio de sesión
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    // Procesar inicio de sesión
    @PostMapping("/login")
    public String iniciarSesion(
            @RequestParam String correo,
            @RequestParam String contrasena,
            HttpSession session,
            Model model) {

        Usuario usuario = usuarioRepository.findByCorreo(correo);

        if (usuario != null &&
                passwordEncoder.matches(contrasena, usuario.getContrasena())) {

            session.setAttribute("usuarioLogueado", usuario.getIdUsuario());

            return "redirect:/";
        }

        model.addAttribute("error", "Correo o contraseña incorrectos.");

        return "login";
    }

    // Mostrar formulario de registro
    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    // Registrar usuario
    @PostMapping("/registro")
    public String registrar(
            @RequestParam String nombre,
            @RequestParam String correo,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String direccion,
            @RequestParam String contrasena,
            RedirectAttributes redirectAttributes) {

        if (usuarioRepository.existsByCorreo(correo)) {

            redirectAttributes.addFlashAttribute(
                    "error", "Este correo ya está registrado."
            );

            return "redirect:/registro";
        }

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setTelefono(telefono);
        usuario.setDireccion(direccion);

        // Cifrar contraseña con BCrypt
        usuario.setContrasena(passwordEncoder.encode(contrasena));

        usuarioRepository.save(usuario);

        redirectAttributes.addFlashAttribute(
                "mensaje", "Registro exitoso. Ahora puedes iniciar sesión."
        );

        return "redirect:/login";
    }

    // Cerrar sesión
    @GetMapping("/logout")
    public String cerrarSesion(
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        session.invalidate();

        redirectAttributes.addFlashAttribute(
                "mensaje", "Has cerrado sesión correctamente."
        );

        return "redirect:/login";
    }
}