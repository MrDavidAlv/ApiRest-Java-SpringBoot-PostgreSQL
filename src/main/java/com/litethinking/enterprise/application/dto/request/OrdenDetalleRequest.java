package com.litethinking.enterprise.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrdenDetalleRequest(
        @NotBlank(message = "Product code is required")
        String productoCodigo,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer cantidad,

        @NotNull(message = "Currency ID is required")
        Integer monedaId
) {
}
