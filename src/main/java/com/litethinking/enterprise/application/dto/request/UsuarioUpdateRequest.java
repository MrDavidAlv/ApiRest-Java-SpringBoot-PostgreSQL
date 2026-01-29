package com.litethinking.enterprise.application.dto.request;

import jakarta.validation.constraints.Size;

public record UsuarioUpdateRequest(
        @Size(max = 100, message = "Display name cannot exceed 100 characters")
        String nombreMostrar,

        @Size(max = 50, message = "Avatar cannot exceed 50 characters")
        String avatar
) {
}
