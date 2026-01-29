package com.litethinking.enterprise.application.dto.response;

public record LoginResponse(
        String token,
        String tipo,
        Long usuarioId,
        String correo,
        String rol,
        String nombreMostrar,
        String avatar,
        long expiraEn
) {
    public static LoginResponse of(String token, Long usuarioId, String correo, String rol, String nombreMostrar, String avatar, long expiraEn) {
        return new LoginResponse(token, "Bearer", usuarioId, correo, rol, nombreMostrar, avatar, expiraEn);
    }
}
