package com.litethinking.enterprise.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ProductoResponse(
        String codigo,
        String nombre,
        String caracteristicas,
        String empresaNit,
        boolean activo,
        String urlImagen,
        List<PrecioResponse> precios,
        LocalDateTime fechaCreacion
) {
}
