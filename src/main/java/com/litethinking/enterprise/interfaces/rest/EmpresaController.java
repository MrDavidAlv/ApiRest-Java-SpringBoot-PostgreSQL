package com.litethinking.enterprise.interfaces.rest;

import com.litethinking.enterprise.application.dto.request.ActualizarEmpresaRequest;
import com.litethinking.enterprise.application.dto.request.CrearEmpresaRequest;
import com.litethinking.enterprise.application.dto.response.EmpresaResponse;
import com.litethinking.enterprise.application.usecase.EmpresaUseCase;
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
@RequestMapping("/api/v1/empresas")
@Tag(name = "Empresas", description = "Company management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class EmpresaController {

    private final EmpresaUseCase empresaUseCase;

    public EmpresaController(EmpresaUseCase empresaUseCase) {
        this.empresaUseCase = empresaUseCase;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new company")
    public ResponseEntity<EmpresaResponse> crear(@Valid @RequestBody CrearEmpresaRequest request) {
        EmpresaResponse response = empresaUseCase.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{nit}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXTERNO')")
    @Operation(summary = "Get company by NIT")
    public ResponseEntity<EmpresaResponse> buscarPorNit(@PathVariable String nit) {
        EmpresaResponse response = empresaUseCase.buscarPorNit(nit);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EXTERNO')")
    @Operation(summary = "Get all active companies")
    public ResponseEntity<List<EmpresaResponse>> buscarTodas() {
        List<EmpresaResponse> response = empresaUseCase.buscarTodas();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{nit}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update company")
    public ResponseEntity<EmpresaResponse> actualizar(
            @PathVariable String nit,
            @Valid @RequestBody ActualizarEmpresaRequest request
    ) {
        EmpresaResponse response = empresaUseCase.actualizar(nit, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{nit}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deactivate company")
    public ResponseEntity<Void> eliminar(@PathVariable String nit) {
        empresaUseCase.eliminar(nit);
        return ResponseEntity.noContent().build();
    }
}
