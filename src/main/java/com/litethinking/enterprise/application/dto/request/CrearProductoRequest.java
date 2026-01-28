package com.litethinking.enterprise.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

public record CrearProductoRequest(
        @NotBlank(message = "Product code is required")
        @Size(max = 50, message = "Product code cannot exceed 50 characters")
        String codigo,

        @NotBlank(message = "Product name is required")
        @Size(max = 150, message = "Product name cannot exceed 150 characters")
        String nombre,

        @Size(max = 1000, message = "Characteristics cannot exceed 1000 characters")
        String caracteristicas,

        @NotBlank(message = "Company NIT is required")
        String empresaNit,

        @NotEmpty(message = "At least one price is required")
        @Valid
        List<PrecioRequest> precios,

        Set<Integer> categoriaIds
) {
}
