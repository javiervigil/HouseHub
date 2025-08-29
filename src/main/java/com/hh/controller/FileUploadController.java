/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 *
 * @author Javier
 */

public class FileUploadController {

    private static final String UPLOAD_DIR = "src/main/resources/uploads/";

    @PostMapping("/upload-file")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {

        // Validar si el archivo está vacío
        if (file.isEmpty()) {
            return new ResponseEntity<>("El archivo está vacío", HttpStatus.BAD_REQUEST);
        }

        try {
            
            //UPLOAD_DIR /  IdCondominio / idEgreso /
            
            // Crear el directorio si no existe
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Obtener el nombre del archivo y la ruta de destino
            String fileName = file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            // Copiar el archivo al directorio de destino
            // Con StandardCopyOption.REPLACE_EXISTING se sobrescribe si existe
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Devolver una respuesta exitosa
            return new ResponseEntity<>("Archivo '" + fileName + "' subido exitosamente.", HttpStatus.OK);

        } catch (IOException e) {
            // Manejar errores de entrada/salida
            return new ResponseEntity<>("Error al subir el archivo: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
