package com.litethinking.enterprise.domain.model.valueobject;

import com.litethinking.enterprise.domain.exception.InvalidValueObjectException;

import java.util.Objects;
import java.util.regex.Pattern;

public final class Nit {

    private static final Pattern NIT_PATTERN = Pattern.compile("^[0-9]{9,15}(-[0-9])?$");
    private static final int MAX_LENGTH = 20;

    private final String value;

    private Nit(String value) {
        this.value = value;
    }

    public static Nit of(String nit) {
        if (nit == null || nit.isBlank()) {
            throw new InvalidValueObjectException("NIT cannot be null or empty");
        }

        String normalized = nit.trim().toUpperCase();

        if (normalized.length() > MAX_LENGTH) {
            throw new InvalidValueObjectException("NIT exceeds maximum length of " + MAX_LENGTH);
        }

        return new Nit(normalized);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nit nit = (Nit) o;
        return Objects.equals(value, nit.value);
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
