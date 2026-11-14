package com.biblioteca.sistema_biblioteca;

import com.biblioteca.sistema_biblioteca.controller.SesionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final SesionInterceptor sesionInterceptor;

    public WebConfig(SesionInterceptor sesionInterceptor) {
        this.sesionInterceptor = sesionInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(sesionInterceptor)
                .addPathPatterns(
                        "/",
                        "/libros",
                        "/api/libros",
                        "/api/libros/**",
                        "/autores/**",
                        "/categorias/**",
                        "/usuarios/**",
                        "/prestamos/**",
                        "/detalles-prestamo/**"
                )
                .excludePathPatterns(
                        "/login",
                        "/registro",
                        "/usuarios/guardar",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/favicon.ico"
                );
    }
}