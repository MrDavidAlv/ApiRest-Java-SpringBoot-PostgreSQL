package com.litethinking.enterprise.application.dto.response;

import java.math.BigDecimal;

public record PrecioResponse(
        Integer monedaId,
        String codigoMoneda,
        BigDecimal precio
) {
}
