package com.litethinking.enterprise.domain.model;

import com.litethinking.enterprise.domain.exception.BusinessRuleViolationException;
import com.litethinking.enterprise.domain.model.valueobject.CodigoProducto;
import com.litethinking.enterprise.domain.model.valueobject.Money;
import com.litethinking.enterprise.domain.model.valueobject.Nit;

import java.time.LocalDateTime;
import java.util.*;

public class Producto {

    private CodigoProducto codigo;
    private String nombre;
    private String caracteristicas;
    private Nit empresaNit;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private Long usuarioCreaId;
    private Map<Integer, Money> precios;
    private Set<Integer> categoriaIds;

    private Producto() {
        this.precios = new HashMap<>();
        this.categoriaIds = new HashSet<>();
    }

    public static Producto crear(
            CodigoProducto codigo,
            String nombre,
            String caracteristicas,
            Nit empresaNit,
            Long usuarioCreaId
    ) {
        Objects.requireNonNull(codigo, "Product code cannot be null");
        Objects.requireNonNull(empresaNit, "Company NIT cannot be null");

        if (nombre == null || nombre.isBlank()) {
            throw new BusinessRuleViolationException("Product name cannot be null or empty");
        }

        Producto producto = new Producto();
        producto.codigo = codigo;
        producto.nombre = nombre;
        producto.caracteristicas = caracteristicas;
        producto.empresaNit = empresaNit;
        producto.activo = true;
        producto.fechaCreacion = LocalDateTime.now();
        producto.usuarioCreaId = usuarioCreaId;

        return producto;
    }

    public void actualizar(String nombre, String caracteristicas) {
        if (!this.activo) {
            throw new BusinessRuleViolationException("Cannot update an inactive product");
        }

        if (nombre == null || nombre.isBlank()) {
            throw new BusinessRuleViolationException("Product name cannot be null or empty");
        }

        this.nombre = nombre;
        this.caracteristicas = caracteristicas;
        this.fechaModificacion = LocalDateTime.now();
    }

    public void agregarPrecio(Integer monedaId, Money precio) {
        Objects.requireNonNull(monedaId, "Currency ID cannot be null");
        Objects.requireNonNull(precio, "Price cannot be null");

        if (!this.activo) {
            throw new BusinessRuleViolationException("Cannot add price to an inactive product");
        }

        this.precios.put(monedaId, precio);
    }

    public void actualizarPrecio(Integer monedaId, Money precio) {
        Objects.requireNonNull(monedaId, "Currency ID cannot be null");
        Objects.requireNonNull(precio, "Price cannot be null");

        if (!this.precios.containsKey(monedaId)) {
            throw new BusinessRuleViolationException("Price for currency " + monedaId + " does not exist");
        }

        this.precios.put(monedaId, precio);
    }

    public void agregarCategoria(Integer categoriaId) {
        Objects.requireNonNull(categoriaId, "Category ID cannot be null");
        this.categoriaIds.add(categoriaId);
    }

    public void removerCategoria(Integer categoriaId) {
        this.categoriaIds.remove(categoriaId);
    }

    public Money obtenerPrecio(Integer monedaId) {
        Money precio = precios.get(monedaId);
        if (precio == null) {
            throw new BusinessRuleViolationException("Product does not have a price in currency: " + monedaId);
        }
        return precio;
    }

    public void desactivar() {
        if (!activo) {
            throw new BusinessRuleViolationException("Product is already inactive");
        }
        this.activo = false;
    }

    public CodigoProducto getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public Nit getEmpresaNit() {
        return empresaNit;
    }

    public boolean isActivo() {
        return activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public Long getUsuarioCreaId() {
        return usuarioCreaId;
    }

    public Map<Integer, Money> getPrecios() {
        return Collections.unmodifiableMap(precios);
    }

    public Set<Integer> getCategoriaIds() {
        return Collections.unmodifiableSet(categoriaIds);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return Objects.equals(codigo, producto.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
