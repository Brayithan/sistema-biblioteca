package com.biblioteca.sistema_biblioteca.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    // Página principal: inicio de sesión
    @GetMapping("/")
    public String inicio(HttpSession session) {

        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }

        return "inicio";
    }

    // Módulo de libros
    @GetMapping("/libros")
    public String libros(HttpSession session) {

        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }

        return "index";
    }
}