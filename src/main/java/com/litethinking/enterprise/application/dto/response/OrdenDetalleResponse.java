package com.litethinking.enterprise.application.dto.response;

import java.math.BigDecimal;

public record OrdenDetalleResponse(
        String productoCodigo,
        String productoNombre,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {
}
