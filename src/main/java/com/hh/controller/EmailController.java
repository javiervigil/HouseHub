/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.controller;

import com.hh.model.User;
import com.hh.repository.UsersRepository;
import com.hh.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")

public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UsersRepository usersRepository;

    @PostMapping("/send-welcome-email")
    public void sendWelcomeEmail(@RequestParam Long userId) {
        User user = usersRepository.findById(userId).orElse(null);
        if (user != null) {
            String subject = "¡Bienvenido a nuestra plataforma!";
            String body = "Hola " + user.getName() + ",\n\nGracias por registrarte...";
            emailService.sendEmail(user.getEmail(), subject, body);
        }
    }

    @PostMapping("/estadocuenta")
    public void estadoCuenta() {
        emailService.sendWelcomeEmail("javiervigil2@hotmail.com", "Javier T.");
    }

}
