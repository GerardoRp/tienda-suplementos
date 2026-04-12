package com.universidad.tienda_suplementos.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void enviarCorreoRegistroHTML(String destinatario, String nombreUsuario) throws MessagingException {
        // Variables que se pasan al HTML
        Context context = new Context();
        context.setVariable("nombre", nombreUsuario);

        // Procesameinto del HTML con las variables
        String contenidoHTML = templateEngine.process("registro-email", context);

        // Config del correo
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setTo(destinatario);
        helper.setSubject("¡Bienvenido a Tienda de Suplementos!");
        helper.setText(contenidoHTML, true);
        // Envio del correo
        mailSender.send(mimeMessage);
    }
}