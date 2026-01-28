package com.litethinking.enterprise.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PrecioRequest(
        @NotNull(message = "Currency ID is required")
        Integer monedaId,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
        BigDecimal precio
) {
}
