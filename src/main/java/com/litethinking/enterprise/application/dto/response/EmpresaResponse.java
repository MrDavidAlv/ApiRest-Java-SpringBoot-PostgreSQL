package com.litethinking.enterprise.application.dto.response;

import java.time.LocalDateTime;

public record EmpresaResponse(
        String nit,
        String nombre,
        String direccion,
        String telefono,
        boolean activo,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
) {
}
