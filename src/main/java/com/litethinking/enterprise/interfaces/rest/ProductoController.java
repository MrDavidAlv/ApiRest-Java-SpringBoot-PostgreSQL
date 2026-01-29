package com.litethinking.enterprise.interfaces.rest;

import com.litethinking.enterprise.application.dto.ProductoFilters;
import com.litethinking.enterprise.application.dto.request.CrearProductoRequest;
import com.litethinking.enterprise.application.dto.response.ProductoResponse;
import com.litethinking.enterprise.application.usecase.ProductoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "Product management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class ProductoController {

    private final ProductoUseCase productoUseCase;

    public ProductoController(ProductoUseCase productoUseCase) {
        this.productoUseCase = productoUseCase;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new product with prices in multiple currencies")
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody CrearProductoRequest request) {
        ProductoResponse response = productoUseCase.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{codigo}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXTERNO')")
    @Operation(summary = "Get product by code")
    public ResponseEntity<ProductoResponse> buscarPorCodigo(@PathVariable String codigo) {
        ProductoResponse response = productoUseCase.buscarPorCodigo(codigo);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EXTERNO')")
    @Operation(summary = "Get products with optional filters")
    public ResponseEntity<List<ProductoResponse>> buscarTodos(
            @Parameter(description = "Search term (name or code)") @RequestParam(required = false) String searchTerm,
            @Parameter(description = "Filter by active status") @RequestParam(required = false) Boolean activo,
            @Parameter(description = "Filter by company NIT") @RequestParam(required = false) String empresaNit,
            @Parameter(description = "Filter by category ID") @RequestParam(required = false) Integer categoriaId
    ) {
        ProductoFilters filters = new ProductoFilters(searchTerm, activo, empresaNit, categoriaId);
        List<ProductoResponse> response = productoUseCase.buscarConFiltros(filters);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/empresa/{nit}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EXTERNO')")
    @Operation(summary = "Get all products by company")
    public ResponseEntity<List<ProductoResponse>> buscarPorEmpresa(@PathVariable String nit) {
        List<ProductoResponse> response = productoUseCase.buscarPorEmpresa(nit);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{codigo}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deactivate product")
    public ResponseEntity<Void> eliminar(@PathVariable String codigo) {
        productoUseCase.eliminar(codigo);
        return ResponseEntity.noContent().build();
    }
}
