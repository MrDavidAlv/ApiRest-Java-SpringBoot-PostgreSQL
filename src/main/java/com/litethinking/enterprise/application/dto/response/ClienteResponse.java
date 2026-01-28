package com.litethinking.enterprise.application.dto.response;

public record ClienteResponse(
    Long id,
    String documento,
    String nombre,
    String correo
) {}
