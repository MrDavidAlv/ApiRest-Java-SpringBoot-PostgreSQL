package com.litethinking.enterprise.domain.model;

import java.util.Objects;

public class EstadoOrden {

    public static final String PENDIENTE = "PENDIENTE";
    public static final String PAGADA = "PAGADA";
    public static final String ANULADA = "ANULADA";

    private Integer id;
    private String nombre;

    private EstadoOrden() {
    }

    public static EstadoOrden of(Integer id, String nombre) {
        EstadoOrden estado = new EstadoOrden();
        estado.id = id;
        estado.nombre = nombre;
        return estado;
    }

    public static EstadoOrden pendiente() {
        return of(1, PENDIENTE);
    }

    public static EstadoOrden pagada() {
        return of(2, PAGADA);
    }

    public static EstadoOrden anulada() {
        return of(3, ANULADA);
    }

    public boolean esPendiente() {
        return PENDIENTE.equals(nombre);
    }

    public boolean esPagada() {
        return PAGADA.equals(nombre);
    }

    public boolean esAnulada() {
        return ANULADA.equals(nombre);
    }

    public boolean puedeTransicionarA(EstadoOrden nuevoEstado) {
        if (this.equals(nuevoEstado)) {
            return false;
        }

        if (this.esPendiente()) {
            return nuevoEstado.esPagada() || nuevoEstado.esAnulada();
        }

        if (this.esPagada() || this.esAnulada()) {
            return false;
        }

        return false;
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EstadoOrden that = (EstadoOrden) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
