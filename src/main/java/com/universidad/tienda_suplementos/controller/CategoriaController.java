package com.universidad.tienda_suplementos.controller;

import com.universidad.tienda_suplementos.entity.Categoria;
import com.universidad.tienda_suplementos.repository.CategoriaRepository;
import com.universidad.tienda_suplementos.service.CategoriaService; // Corregido a minúscula
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

   
    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    //   Metodos nuevos usando capa service

    @PostMapping("/crear")
    public ResponseEntity<Categoria> crearCategoriaServicio(@RequestBody Categoria categoria) { // Nombre cambiado
        Categoria nuevaCategoria = categoriaService.guardarCategoria(categoria);
        return ResponseEntity.ok(nuevaCategoria);
    }

    @GetMapping("/activas")
    public List<Categoria> listarActivas() {
        return categoriaService.obtenerCategoriasActivas();
    }

    @GetMapping("/buscar")
    public ResponseEntity<Categoria> buscarPorNombre(@RequestParam String nombre) {
        return categoriaService.buscarCategoriaPorNombre(nombre)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    //   Metodos de la entrega anterior usando repository

    @GetMapping
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerPorId(@PathVariable Integer id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        return categoria.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Categoria crearCategoriaBasica(@RequestBody Categoria nuevaCategoria) { // Nombre cambiado
        return categoriaRepository.save(nuevaCategoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable Integer id, @RequestBody Categoria categoriaActualizada) {
        return categoriaRepository.findById(id).map(categoria -> {
            categoria.setNombre(categoriaActualizada.getNombre());
            categoria.setSlug(categoriaActualizada.getSlug());
            categoria.setDescripcion(categoriaActualizada.getDescripcion());
            return ResponseEntity.ok(categoriaRepository.save(categoria));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Categoria> desactivarCategoria(@PathVariable Integer id) {
        return categoriaRepository.findById(id).map(categoria -> {
            categoria.setActivo(false);
            return ResponseEntity.ok(categoriaRepository.save(categoria));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/activar")
    public ResponseEntity<Categoria> activarCategoria(@PathVariable Integer id) {
        return categoriaRepository.findById(id).map(categoria -> {
            categoria.setActivo(true);
            return ResponseEntity.ok(categoriaRepository.save(categoria));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}