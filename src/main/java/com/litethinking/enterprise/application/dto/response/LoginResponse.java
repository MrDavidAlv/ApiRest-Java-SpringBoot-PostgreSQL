package com.litethinking.enterprise.application.dto.response;

public record LoginResponse(
        String token,
        String tipo,
        Long usuarioId,
        String correo,
        String rol,
        long expiraEn
) {
    public static LoginResponse of(String token, Long usuarioId, String correo, String rol, long expiraEn) {
        return new LoginResponse(token, "Bearer", usuarioId, correo, rol, expiraEn);
    }
}
