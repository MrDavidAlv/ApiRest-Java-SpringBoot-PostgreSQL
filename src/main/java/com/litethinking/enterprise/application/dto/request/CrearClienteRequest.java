package com.litethinking.enterprise.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CrearClienteRequest(
    @NotBlank(message = "El documento es obligatorio")
    String documento,
    
    @NotBlank(message = "El nombre es obligatorio")
    String nombre,
    
    @Email(message = "El correo debe ser válido")
    String correo
) {}
