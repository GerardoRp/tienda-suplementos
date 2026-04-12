package com.universidad.tienda_suplementos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice //Anotación para definir esta clase como un manejador global de excepciones para controladores REST, permitiendo capturar y manejar errores de manera centralizada en toda la aplicación
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class) //Manejador específico para capturar excepciones de validación de argumentos, que ocurren cuando los datos enviados en una solicitud no cumplen con las restricciones definidas en el DTO
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> //  Iteramos sobre los errores de validación y los agregamos a un mapa con el nombre del campo y el mensaje de error correspondiente, para luego incluir esta información en la respuesta al cliente
            errors.put(error.getField(), error.getDefaultMessage())
        );

        Map<String, Object> response = new HashMap<>(); // Creamos un mapa para la respuesta que se enviará al cliente, incluyendo el estado de error, un mensaje general y los detalles específicos de los errores de validación
        response.put("status", "error");
        response.put("message", "Validation failed");
        response.put("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // Devolvemos una respuesta con el estado HTTP 400 (Bad Request) y el cuerpo que contiene los detalles de los errores de validación, para que el cliente pueda entender qué salió mal con los datos enviados
    }

    @ExceptionHandler(Exception.class) //Manejador general para capturar cualquier otra excepción que no haya sido manejada específicamente, proporcionando una respuesta genérica de error interno del servidor
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) { // Capturamos cualquier excepción no manejada y preparamos una respuesta genérica indicando que ocurrió un error interno en el servidor, incluyendo el mensaje de la excepción para facilitar la depuración
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "Ocurrió un error interno en el servidor");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Devolvemos una respuesta con el estado HTTP 500 (Internal Server Error) y el cuerpo que contiene el mensaje de error genérico, para informar al cliente que algo salió mal en el servidor
    }
}