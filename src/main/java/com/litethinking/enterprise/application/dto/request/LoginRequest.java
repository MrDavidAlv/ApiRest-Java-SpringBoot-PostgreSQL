package com.litethinking.enterprise.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String correo,

        @NotBlank(message = "Password is required")
        String password,

        @NotBlank(message = "reCAPTCHA token is required")
        String recaptchaToken
) {
}
