package com.litethinking.enterprise.application.dto;

public record ProductoFilters(
        String searchTerm,
        Boolean activo,
        String empresaNit,
        Integer categoriaId
) {
    public static ProductoFilters empty() {
        return new ProductoFilters(null, null, null, null);
    }
}
