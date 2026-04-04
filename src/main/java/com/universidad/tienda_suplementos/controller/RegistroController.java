package com.universidad.tienda_suplementos.controller;

import com.universidad.tienda_suplementos.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.universidad.tienda_suplementos.dto.RegistroRequestDTO;

@RestController
@RequestMapping("/api/registro")
public class RegistroController {

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<String> registrarUsuario(@RequestBody RegistroRequestDTO request) {
        String correo = request.getCorreo();
        String nombre = request.getNombre();

        try {
            // Envio del correo
            emailService.enviarCorreoRegistroHTML(correo, nombre);
            return ResponseEntity.ok("Usuario registrado y correo enviado a: " + correo);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}