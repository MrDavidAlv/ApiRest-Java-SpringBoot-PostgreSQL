package com.litethinking.enterprise.application.dto.response;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String correo,
        String nombreMostrar,
        String avatar,
        String rol,
        boolean activo,
        LocalDateTime fechaCreacion
) {
}
