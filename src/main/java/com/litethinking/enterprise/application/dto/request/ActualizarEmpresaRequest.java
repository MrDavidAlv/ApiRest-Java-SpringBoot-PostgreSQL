package com.litethinking.enterprise.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ActualizarEmpresaRequest(
        @NotBlank(message = "Company name is required")
        @Size(max = 150, message = "Company name cannot exceed 150 characters")
        String nombre,

        @Size(max = 255, message = "Address cannot exceed 255 characters")
        String direccion,

        @Size(max = 20, message = "Phone cannot exceed 20 characters")
        String telefono
) {
}
