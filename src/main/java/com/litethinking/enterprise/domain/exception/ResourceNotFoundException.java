package com.litethinking.enterprise.domain.exception;

public final class ResourceNotFoundException extends DomainException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException forResource(String resourceType, Object identifier) {
        return new ResourceNotFoundException(
                String.format("%s not found with identifier: %s", resourceType, identifier)
        );
    }
}
