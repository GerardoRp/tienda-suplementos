package com.universidad.tienda_suplementos.controller;

import com.universidad.tienda_suplementos.entity.Categoria;
import com.universidad.tienda_suplementos.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    //1 Listar todas las categorías
    @GetMapping
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    //2 Buscar una categoría por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerPorId(@PathVariable Integer id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        return categoria.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3 Crear una nueva categoría
    @PostMapping
    public Categoria crearCategoria(@RequestBody Categoria nuevaCategoria) {
        return categoriaRepository.save(nuevaCategoria);
    }

    // 4 Actualizar una categoría existente
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable Integer id, @RequestBody Categoria categoriaActualizada) {
        return categoriaRepository.findById(id).map(categoria -> {
            categoria.setNombre(categoriaActualizada.getNombre());
            categoria.setSlug(categoriaActualizada.getSlug());
            categoria.setDescripcion(categoriaActualizada.getDescripcion());
            return ResponseEntity.ok(categoriaRepository.save(categoria));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 5 Borrado lógico
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Categoria> desactivarCategoria(@PathVariable Integer id) {
        return categoriaRepository.findById(id).map(categoria -> {
            categoria.setActivo(false);
            return ResponseEntity.ok(categoriaRepository.save(categoria));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 6 Reactivar categoría
    @PutMapping("/{id}/activar")
    public ResponseEntity<Categoria> activarCategoria(@PathVariable Integer id) {
        return categoriaRepository.findById(id).map(categoria -> {
            categoria.setActivo(true);
            return ResponseEntity.ok(categoriaRepository.save(categoria));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
