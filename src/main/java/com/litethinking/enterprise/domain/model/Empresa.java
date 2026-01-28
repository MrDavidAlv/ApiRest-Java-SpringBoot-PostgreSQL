package com.litethinking.enterprise.domain.model;

import com.litethinking.enterprise.domain.exception.BusinessRuleViolationException;
import com.litethinking.enterprise.domain.model.valueobject.Nit;

import java.time.LocalDateTime;
import java.util.Objects;

public class Empresa {

    private Nit nit;
    private String nombre;
    private String direccion;
    private String telefono;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private Long usuarioCreaId;
    private Long usuarioModificaId;

    private Empresa() {
    }

    public static Empresa crear(Nit nit, String nombre, String direccion, String telefono, Long usuarioCreaId) {
        Objects.requireNonNull(nit, "NIT cannot be null");
        if (nombre == null || nombre.isBlank()) {
            throw new BusinessRuleViolationException("Company name cannot be null or empty");
        }

        Empresa empresa = new Empresa();
        empresa.nit = nit;
        empresa.nombre = nombre;
        empresa.direccion = direccion;
        empresa.telefono = telefono;
        empresa.activo = true;
        empresa.fechaCreacion = LocalDateTime.now();
        empresa.usuarioCreaId = usuarioCreaId;

        return empresa;
    }

    public void actualizar(String nombre, String direccion, String telefono, Long usuarioModificaId) {
        if (!this.activo) {
            throw new BusinessRuleViolationException("Cannot update an inactive company");
        }

        if (nombre == null || nombre.isBlank()) {
            throw new BusinessRuleViolationException("Company name cannot be null or empty");
        }

        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.fechaModificacion = LocalDateTime.now();
        this.usuarioModificaId = usuarioModificaId;
    }

    public void desactivar() {
        if (!activo) {
            throw new BusinessRuleViolationException("Company is already inactive");
        }
        this.activo = false;
    }

    public Nit getNit() {
        return nit;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
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

    public Long getUsuarioModificaId() {
        return usuarioModificaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empresa empresa = (Empresa) o;
        return Objects.equals(nit, empresa.nit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nit);
    }
}
