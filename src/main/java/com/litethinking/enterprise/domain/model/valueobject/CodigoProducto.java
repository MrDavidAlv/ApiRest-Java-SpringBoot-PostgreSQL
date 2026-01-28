package com.litethinking.enterprise.domain.model.valueobject;

import com.litethinking.enterprise.domain.exception.InvalidValueObjectException;

import java.util.Objects;

public final class CodigoProducto {

    private static final int MAX_LENGTH = 50;

    private final String value;

    private CodigoProducto(String value) {
        this.value = value;
    }

    public static CodigoProducto of(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new InvalidValueObjectException("Product code cannot be null or empty");
        }

        String normalized = codigo.trim().toUpperCase();

        if (normalized.length() > MAX_LENGTH) {
            throw new InvalidValueObjectException("Product code exceeds maximum length of " + MAX_LENGTH);
        }

        return new CodigoProducto(normalized);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodigoProducto that = (CodigoProducto) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
