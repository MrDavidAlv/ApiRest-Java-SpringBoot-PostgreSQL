package com.litethinking.enterprise.interfaces.rest;

import com.litethinking.enterprise.application.dto.request.ChangePasswordRequest;
import com.litethinking.enterprise.application.dto.request.ForgotPasswordRequest;
import com.litethinking.enterprise.application.dto.request.LoginRequest;
import com.litethinking.enterprise.application.dto.request.ResetPasswordRequest;
import com.litethinking.enterprise.application.dto.response.LoginResponse;
import com.litethinking.enterprise.application.usecase.AuthenticationUseCase;
import com.litethinking.enterprise.application.usecase.ChangePasswordUseCase;
import com.litethinking.enterprise.application.usecase.ForgotPasswordUseCase;
import com.litethinking.enterprise.application.usecase.ResetPasswordUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthenticationController {

    private final AuthenticationUseCase authenticationUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final ForgotPasswordUseCase forgotPasswordUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;

    public AuthenticationController(
            AuthenticationUseCase authenticationUseCase,
            ChangePasswordUseCase changePasswordUseCase,
            ForgotPasswordUseCase forgotPasswordUseCase,
            ResetPasswordUseCase resetPasswordUseCase
    ) {
        this.authenticationUseCase = authenticationUseCase;
        this.changePasswordUseCase = changePasswordUseCase;
        this.forgotPasswordUseCase = forgotPasswordUseCase;
        this.resetPasswordUseCase = resetPasswordUseCase;
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "422", description = "reCAPTCHA validation failed")
    })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authenticationUseCase.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Change password", description = "Change the password for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data or passwords don't match"),
            @ApiResponse(responseCode = "401", description = "Invalid current password or user not authenticated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Map<String, String>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        changePasswordUseCase.execute(request);
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password", description = "Generate a password reset token for the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reset token generated (if email exists)"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        String token = forgotPasswordUseCase.execute(request);

        if (token != null) {
            return ResponseEntity.ok(Map.of(
                    "message", "Password reset token generated successfully",
                    "token", token
            ));
        }

        return ResponseEntity.ok(Map.of("message", "If the email exists, a reset token will be sent"));
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Reset password using a valid token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data, passwords don't match, or invalid/expired token"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        resetPasswordUseCase.execute(request);
        return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
    }
}
