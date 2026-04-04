package com.universidad.tienda_suplementos.controller;

import com.universidad.tienda_suplementos.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/registro")
public class RegistroController {

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<String> registrarUsuario(@RequestBody Map<String, String> request) {
        String correo = request.get("correo");
        String nombre = request.get("nombre");

        try {
            // Simulacion de guardado en BD
            
            // Envio del correo
            emailService.enviarCorreoRegistroHTML(correo, nombre);
            
            return ResponseEntity.ok("Usuario registrado y correo HTML enviado con éxito a: " + correo);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al enviar el correo: " + e.getMessage());
        }
    }
}