package com.universidad.tienda_suplementos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Marcas")
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marca")
    private Integer idMarca;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "logo_url")
    private String logoUrl;

    private Boolean activo = true;

   
    public Marca() {}

    // Getters y Setters
    public Integer getIdMarca() { return idMarca; }
    public void setIdMarca(Integer idMarca) { this.idMarca = idMarca; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}