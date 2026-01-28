package com.litethinking.enterprise.domain.model;

import com.litethinking.enterprise.domain.exception.BusinessRuleViolationException;
import com.litethinking.enterprise.domain.model.valueobject.Email;

import java.util.Objects;

public class Cliente {

    private Long id;
    private String documento;
    private String nombre;
    private Email correo;

    private Cliente() {
    }

    public static Cliente crear(String documento, String nombre, Email correo) {
        if (documento == null || documento.isBlank()) {
            throw new BusinessRuleViolationException("Client document cannot be null or empty");
        }

        if (nombre == null || nombre.isBlank()) {
            throw new BusinessRuleViolationException("Client name cannot be null or empty");
        }

        Cliente cliente = new Cliente();
        cliente.documento = documento.trim();
        cliente.nombre = nombre;
        cliente.correo = correo;

        return cliente;
    }

    public void actualizar(String nombre, Email correo) {
        if (nombre == null || nombre.isBlank()) {
            throw new BusinessRuleViolationException("Client name cannot be null or empty");
        }

        this.nombre = nombre;
        this.correo = correo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNombre() {
        return nombre;
    }

    public Email getCorreo() {
        return correo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
