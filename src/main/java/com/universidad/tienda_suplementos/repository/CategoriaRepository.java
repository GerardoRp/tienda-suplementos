package com.universidad.tienda_suplementos.repository;

import com.universidad.tienda_suplementos.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    
    // M1 BuscaR una categoría por su nombre exacto
    Optional<Categoria> findByNombre(String nombre);

    // M2: Busca todas las categorías que estén activas
    List<Categoria> findByActivoTrue();

    // M3 Busca categorías que contengan una palabra
    List<Categoria> findByNombreContainingIgnoreCase(String palabra);
}