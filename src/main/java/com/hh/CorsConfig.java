/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Aplica la configuración de CORS a todos los endpoints de tu API (/**)
        registry.addMapping("/**")
                // Permite solicitudes desde cualquier origen
                .allowedOrigins("*")
                // Permite los métodos HTTP GET, POST, PUT, DELETE, etc.
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // Permite todos los encabezados
                .allowedHeaders("*");
    }
}
