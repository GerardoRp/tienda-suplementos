package com.universidad.tienda_suplementos.service;

import com.universidad.tienda_suplementos.entity.Categoria;
import com.universidad.tienda_suplementos.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Guardar una nueva categoría
    public Categoria guardarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    // Obtener solo las activas
    public List<Categoria> obtenerCategoriasActivas() {
        return categoriaRepository.findByActivoTrue();
    }

    // Buscar por nombre exacto
    public Optional<Categoria> buscarCategoriaPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }
}
