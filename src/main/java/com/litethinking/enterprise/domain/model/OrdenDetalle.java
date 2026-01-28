package com.litethinking.enterprise.domain.model;

import com.litethinking.enterprise.domain.exception.BusinessRuleViolationException;
import com.litethinking.enterprise.domain.model.valueobject.CodigoProducto;
import com.litethinking.enterprise.domain.model.valueobject.Money;

import java.util.Objects;

public class OrdenDetalle {

    private Long id;
    private CodigoProducto productocodigo;
    private int cantidad;
    private Money precioUnitarioHistorico;

    private OrdenDetalle() {
    }

    public static OrdenDetalle crear(CodigoProducto productoCodigo, int cantidad, Money precioUnitario) {
        Objects.requireNonNull(productoCodigo, "Product code cannot be null");
        Objects.requireNonNull(precioUnitario, "Unit price cannot be null");

        if (cantidad <= 0) {
            throw new BusinessRuleViolationException("Quantity must be greater than zero");
        }

        OrdenDetalle detalle = new OrdenDetalle();
        detalle.productocodigo = productoCodigo;
        detalle.cantidad = cantidad;
        detalle.precioUnitarioHistorico = precioUnitario;

        return detalle;
    }

    public Money calcularSubtotal() {
        return precioUnitarioHistorico.multiply(cantidad);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CodigoProducto getProductoCodigo() {
        return productocodigo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public Money getPrecioUnitarioHistorico() {
        return precioUnitarioHistorico;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdenDetalle that = (OrdenDetalle) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
