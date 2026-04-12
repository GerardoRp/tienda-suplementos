package com.universidad.tienda_suplementos.controller;

import com.universidad.tienda_suplementos.dto.RegistroRequestDTO;
import com.universidad.tienda_suplementos.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController //Anotación para indicar que esta clase es un controlador REST, lo que significa que manejará solicitudes HTTP y devolverá respuestas en formato JSON o similar
@RequestMapping("/api/registro") //Definición de la ruta base para el controlador, lo que significa que todas las solicitudes a este controlador deben comenzar con "/api/registro"
public class RegistroController {

    @Autowired //Inyección de dependencia para el servicio de correo electrónico, lo que permite utilizar sus métodos para enviar correos sin tener que crear una instancia manualmente
    private EmailService emailService;

    @PostMapping(consumes = "multipart/form-data") //Definición de un endpoint POST que acepta solicitudes con contenido multipart/form-data, lo que es común para formularios que incluyen archivos o datos complejos
    public ResponseEntity<Map<String, Object>> registrarUsuario( //@Valid para activar la validación de los datos recibidos en el DTO, y BindingResult para capturar cualquier error de validación que pueda ocurrir
            @Valid @ModelAttribute RegistroRequestDTO request,
            BindingResult bindingResult //BindingResult para capturar los errores de validación que puedan ocurrir al procesar el DTO anotado con @Valid
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>(); //Creación de un mapa para almacenar los errores de validación, donde la clave es el nombre del campo y el valor es el mensaje de error correspondiente

            bindingResult.getFieldErrors().forEach(error -> //Iteración sobre los errores de validación para llenar el mapa de errores con el nombre del campo y el mensaje de error
                errors.put(error.getField(), error.getDefaultMessage())
            );

            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Validation failed");
            response.put("errors", errors);

            return ResponseEntity.badRequest().body(response);
        }

        try {
            emailService.enviarCorreoRegistroHTML(request.getEmail(), request.getName()); //Llamada al servicio de correo electrónico para enviar un correo de registro utilizando el método enviarCorreoRegistroHTML, pasando el correo electrónico y el nombre del usuario como parámetros

            Map<String, Object> response = new HashMap<>(); //  Creación de un mapa para la respuesta que se enviará al cliente, indicando el estado de la operación y un mensaje informativo
            response.put("status", "success");
            response.put("message", "Usuario registrado y correo enviado a: " + request.getEmail());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error interno al procesar el registro: " + e.getMessage());

            return ResponseEntity.internalServerError().body(response);
        }
    }
}