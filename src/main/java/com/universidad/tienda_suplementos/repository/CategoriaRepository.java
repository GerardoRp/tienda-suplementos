package com.universidad.tienda_suplementos.repository;

import com.universidad.tienda_suplementos.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    
}