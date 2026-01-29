package com.litethinking.enterprise.domain.exception;

public final class InvalidCredentialsException extends DomainException {

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public static InvalidCredentialsException withDefaultMessage() {
        return new InvalidCredentialsException("Invalid credentials");
    }
}
