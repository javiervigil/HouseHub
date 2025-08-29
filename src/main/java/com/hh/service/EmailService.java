/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import org.springframework.mail.SimpleMailMessage;

/**
 *
 * @author Javier
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@tu-dominio.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendWelcomeEmail(String toEmail, String userName) {
        try {
            // 1. Prepara el contexto con los datos dinámicos
            Context context = new Context();
            context.setVariable("userName", userName);
            context.setVariable("userEmail", toEmail);

            // 2. Procesa el template con el contexto
            String htmlContent = templateEngine.process("EstadoCuenta.html", context);

            // 3. Crea y envía el MimeMessage
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            helper.setTo(toEmail);
            helper.setSubject("¡Bienvenido a nuestra plataforma!");
            helper.setText(htmlContent, true); // El 'true' indica que el contenido es HTML

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
