package com.universidad.tienda_suplementos.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegistroRequestDTO {
    //Definición de campos con anotaciones de validación para asegurar que los datos recibidos en la solicitud cumplen con ciertos criterios, como no estar vacíos, tener un formato específico o estar dentro de un rango permitido
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 80, message = "El nombre debe tener entre 2 y 80 caracteres")
    @Pattern(
        regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü\\s]+$", //Expresión regular para permitir solo letras (incluyendo acentos y caracteres especiales) y espacios en el nombre
        message = "El nombre solo puede contener letras y espacios"
    )
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    @Pattern(
        regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü\\s]+$",
        message = "El apellido solo puede contener letras y espacios"
    )
    private String lastName;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")//Anotación para validar que el campo de correo tenga un formato de email válido
    @Size(max = 120, message = "El correo no puede exceder 120 caracteres")
    private String email;

    @NotBlank(message = "La cuenta es obligatoria")
    @Pattern(                                       //Expresión regular para validar que la cuenta tenga entre 8 y 12 dígitos numéricos, asegurando que solo se acepten números y que la longitud sea adecuada
        regexp = "^[0-9]{8,12}$",                  //@Pattern para validar que la cuenta solo contenga dígitos y tenga una longitud entre 8 y 12 caracteres
        message = "La cuenta debe tener entre 8 y 12 dígitos"
    )
    private String account;

    @NotNull(message = "El promedio es obligatorio")
    @DecimalMin(value = "0.0", message = "El promedio no puede ser menor que 0") //Anotación para validar que el promedio sea un número decimal y no sea menor que 0, asegurando que se proporcione un valor válido para el promedio
    @DecimalMax(value = "10.0", message = "El promedio no puede ser mayor que 10")
    private Double avg;

    @NotBlank(message = "El país es obligatorio")
    @Size(max = 80, message = "El país no puede exceder 80 caracteres")
    private String country;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Double getAvg() {
        return avg;
    }

    public void setAvg(Double avg) {
        this.avg = avg;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}