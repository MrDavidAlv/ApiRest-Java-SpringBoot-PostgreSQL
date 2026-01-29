package com.litethinking.enterprise.interfaces.rest;

import com.litethinking.enterprise.application.dto.request.UsuarioCreateRequest;
import com.litethinking.enterprise.application.dto.request.UsuarioUpdateRequest;
import com.litethinking.enterprise.application.dto.response.UsuarioResponse;
import com.litethinking.enterprise.application.usecase.CreateUsuarioUseCase;
import com.litethinking.enterprise.application.usecase.GetUsuarioByIdUseCase;
import com.litethinking.enterprise.application.usecase.ListUsuariosUseCase;
import com.litethinking.enterprise.application.usecase.ToggleUsuarioStatusUseCase;
import com.litethinking.enterprise.application.usecase.UpdateUsuarioUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "User management endpoints (Admin only)")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {

    private final CreateUsuarioUseCase createUsuarioUseCase;
    private final GetUsuarioByIdUseCase getUsuarioByIdUseCase;
    private final ListUsuariosUseCase listUsuariosUseCase;
    private final UpdateUsuarioUseCase updateUsuarioUseCase;
    private final ToggleUsuarioStatusUseCase toggleUsuarioStatusUseCase;

    public UsuarioController(
            CreateUsuarioUseCase createUsuarioUseCase,
            GetUsuarioByIdUseCase getUsuarioByIdUseCase,
            ListUsuariosUseCase listUsuariosUseCase,
            UpdateUsuarioUseCase updateUsuarioUseCase,
            ToggleUsuarioStatusUseCase toggleUsuarioStatusUseCase
    ) {
        this.createUsuarioUseCase = createUsuarioUseCase;
        this.getUsuarioByIdUseCase = getUsuarioByIdUseCase;
        this.listUsuariosUseCase = listUsuariosUseCase;
        this.updateUsuarioUseCase = updateUsuarioUseCase;
        this.toggleUsuarioStatusUseCase = toggleUsuarioStatusUseCase;
    }

    @GetMapping
    @Operation(summary = "List all users", description = "Retrieve all users in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required")
    })
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        List<UsuarioResponse> usuarios = listUsuariosUseCase.execute();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a single user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UsuarioResponse> obtenerUsuario(@PathVariable Long id) {
        UsuarioResponse usuario = getUsuarioByIdUseCase.execute(id);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    @Operation(summary = "Create external user", description = "Create a new external user (EXTERNO role)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "409", description = "User with email already exists")
    })
    public ResponseEntity<UsuarioResponse> crearUsuario(@Valid @RequestBody UsuarioCreateRequest request) {
        UsuarioResponse usuario = createUsuarioUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update user display name and avatar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UsuarioResponse> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateRequest request
    ) {
        UsuarioResponse usuario = updateUsuarioUseCase.execute(id, request);
        return ResponseEntity.ok(usuario);
    }

    @PatchMapping("/{id}/toggle-status")
    @Operation(summary = "Toggle user status", description = "Activate or deactivate a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User status toggled successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UsuarioResponse> toggleStatus(@PathVariable Long id) {
        UsuarioResponse usuario = toggleUsuarioStatusUseCase.execute(id);
        return ResponseEntity.ok(usuario);
    }
}
