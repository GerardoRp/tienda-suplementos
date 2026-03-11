package com.universidad.tienda_suplementos.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/basico")
public class TiendaController {

    // 1. GET
    @GetMapping("/estado")
    public String verificarEstado() { return "Servidor funcionando."; }

    // 2. GET
    @GetMapping("/info")
    public String obtenerInfo() { return "Tienda de Suplementos v1.0"; }

    // 3. POST
    @PostMapping("/enviar")
    public String recibirDatos(@RequestBody String datos) { return "Datos recibidos: " + datos; }

    // 4. PUT
    @PutMapping("/actualizar/1")
    public String actualizarDatos() { return "Registro 1 actualizado."; }

    // 5. PUT
    @PutMapping("/desactivar/1")
    public String desactivarDatos() { return "Registro 1 desactivado."; }
}