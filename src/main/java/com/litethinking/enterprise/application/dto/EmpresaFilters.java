package com.litethinking.enterprise.application.dto;

public record EmpresaFilters(
        String searchTerm,
        Boolean activo
) {
    public static EmpresaFilters empty() {
        return new EmpresaFilters(null, null);
    }
}
