package com.biblioteca.sistema_biblioteca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador encargado de mostrar la interfaz web del sistema.
 */
@Controller
public class WebController {

    @GetMapping("/")
    public String inicio() {
        return "index";
    }
}