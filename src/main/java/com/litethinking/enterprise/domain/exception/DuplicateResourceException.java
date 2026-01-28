package com.litethinking.enterprise.domain.exception;

public final class DuplicateResourceException extends DomainException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public static DuplicateResourceException forResource(String resourceType, Object identifier) {
        return new DuplicateResourceException(
                String.format("%s already exists with identifier: %s", resourceType, identifier)
        );
    }
}
