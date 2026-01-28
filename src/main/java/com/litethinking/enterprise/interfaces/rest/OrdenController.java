package com.litethinking.enterprise.interfaces.rest;

import com.litethinking.enterprise.application.dto.request.CrearOrdenRequest;
import com.litethinking.enterprise.application.dto.response.OrdenResponse;
import com.litethinking.enterprise.application.usecase.OrdenUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ordenes")
@Tag(name = "Ordenes", description = "Order management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class OrdenController {

    private final OrdenUseCase ordenUseCase;

    public OrdenController(OrdenUseCase ordenUseCase) {
        this.ordenUseCase = ordenUseCase;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new order")
    public ResponseEntity<OrdenResponse> crear(@Valid @RequestBody CrearOrdenRequest request) {
        OrdenResponse response = ordenUseCase.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<OrdenResponse> buscarPorId(@PathVariable Long id) {
        OrdenResponse response = ordenUseCase.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all orders")
    public ResponseEntity<List<OrdenResponse>> buscarTodas() {
        List<OrdenResponse> response = ordenUseCase.buscarTodas();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change order state")
    public ResponseEntity<OrdenResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestParam Integer nuevoEstadoId
    ) {
        OrdenResponse response = ordenUseCase.cambiarEstado(id, nuevoEstadoId);
        return ResponseEntity.ok(response);
    }
}
