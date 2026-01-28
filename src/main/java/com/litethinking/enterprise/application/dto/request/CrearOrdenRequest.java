package com.litethinking.enterprise.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CrearOrdenRequest(
        @NotNull(message = "Client ID is required")
        Long clienteId,

        @NotEmpty(message = "At least one item is required")
        @Valid
        List<OrdenDetalleRequest> detalles
) {
}
