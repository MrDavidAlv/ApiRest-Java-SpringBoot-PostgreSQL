package com.litethinking.enterprise.domain.model;

import java.util.Objects;

public class Rol {

    public static final String ADMIN = "ADMIN";
    public static final String EXTERNO = "EXTERNO";

    private Integer id;
    private String nombre;
    private String descripcion;

    private Rol() {
    }

    public static Rol of(Integer id, String nombre, String descripcion) {
        Rol rol = new Rol();
        rol.id = id;
        rol.nombre = nombre;
        rol.descripcion = descripcion;
        return rol;
    }

    public boolean isAdmin() {
        return ADMIN.equals(nombre);
    }

    public boolean isExterno() {
        return EXTERNO.equals(nombre);
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rol rol = (Rol) o;
        return Objects.equals(id, rol.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
