package com.litethinking.enterprise.domain.model;

import com.litethinking.enterprise.domain.exception.BusinessRuleViolationException;
import com.litethinking.enterprise.domain.model.valueobject.CodigoProducto;
import com.litethinking.enterprise.domain.model.valueobject.Money;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Orden {

    private Long id;
    private Long clienteId;
    private EstadoOrden estado;
    private LocalDateTime fechaOrden;
    private Long usuarioCreaId;
    private List<OrdenDetalle> detalles;

    private Orden() {
        this.detalles = new ArrayList<>();
    }

    public static Orden crear(Long clienteId, EstadoOrden estadoInicial, Long usuarioCreaId) {
        Objects.requireNonNull(clienteId, "Client ID cannot be null");
        Objects.requireNonNull(estadoInicial, "Initial state cannot be null");

        Orden orden = new Orden();
        orden.clienteId = clienteId;
        orden.estado = estadoInicial;
        orden.fechaOrden = LocalDateTime.now();
        orden.usuarioCreaId = usuarioCreaId;

        return orden;
    }

    public void agregarDetalle(CodigoProducto codigoProducto, int cantidad, Money precioUnitario) {
        if (this.estado.esPagada() || this.estado.esAnulada()) {
            throw new BusinessRuleViolationException("Cannot add items to an order in state: " + estado.getNombre());
        }

        OrdenDetalle detalle = OrdenDetalle.crear(codigoProducto, cantidad, precioUnitario);
        this.detalles.add(detalle);
    }

    public void cambiarEstado(EstadoOrden nuevoEstado) {
        Objects.requireNonNull(nuevoEstado, "New state cannot be null");

        if (!estado.puedeTransicionarA(nuevoEstado)) {
            throw new BusinessRuleViolationException(
                    String.format("Cannot transition from %s to %s", estado.getNombre(), nuevoEstado.getNombre())
            );
        }

        this.estado = nuevoEstado;
    }

    public void anular() {
        if (estado.esPagada()) {
            throw new BusinessRuleViolationException("Cannot cancel a paid order");
        }

        if (estado.esAnulada()) {
            throw new BusinessRuleViolationException("Order is already cancelled");
        }

        this.estado = EstadoOrden.anulada();
    }

    public Money calcularTotal() {
        if (detalles.isEmpty()) {
            throw new BusinessRuleViolationException("Cannot calculate total for an empty order");
        }

        Money total = detalles.get(0).calcularSubtotal();
        for (int i = 1; i < detalles.size(); i++) {
            total = total.add(detalles.get(i).calcularSubtotal());
        }

        return total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public LocalDateTime getFechaOrden() {
        return fechaOrden;
    }

    public Long getUsuarioCreaId() {
        return usuarioCreaId;
    }

    public List<OrdenDetalle> getDetalles() {
        return Collections.unmodifiableList(detalles);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orden orden = (Orden) o;
        return Objects.equals(id, orden.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
