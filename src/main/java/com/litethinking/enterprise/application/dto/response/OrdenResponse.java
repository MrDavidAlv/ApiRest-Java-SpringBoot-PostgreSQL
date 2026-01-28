package com.litethinking.enterprise.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrdenResponse(
        Long id,
        Long clienteId,
        String clienteNombre,
        String estado,
        LocalDateTime fechaOrden,
        List<OrdenDetalleResponse> detalles,
        BigDecimal total,
        String moneda
) {
}
