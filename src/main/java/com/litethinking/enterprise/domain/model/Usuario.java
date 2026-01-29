package com.litethinking.enterprise.domain.model;

import com.litethinking.enterprise.domain.exception.BusinessRuleViolationException;
import com.litethinking.enterprise.domain.model.valueobject.Email;

import java.time.LocalDateTime;
import java.util.Objects;

public class Usuario {

    private Long id;
    private Email correo;
    private String passwordHash;
    private Rol rol;
    private boolean activo;
    private String nombreMostrar;
    private String avatar;
    private LocalDateTime fechaCreacion;
    private Long usuarioCreaId;

    private Usuario() {
    }

    public static Usuario crear(Email correo, String passwordHash, Rol rol, Long usuarioCreaId) {
        Objects.requireNonNull(correo, "Email cannot be null");
        Objects.requireNonNull(passwordHash, "Password hash cannot be null");
        Objects.requireNonNull(rol, "Role cannot be null");

        if (passwordHash.isBlank()) {
            throw new BusinessRuleViolationException("Password hash cannot be empty");
        }

        Usuario usuario = new Usuario();
        usuario.correo = correo;
        usuario.passwordHash = passwordHash;
        usuario.rol = rol;
        usuario.activo = true;
        usuario.fechaCreacion = LocalDateTime.now();
        usuario.usuarioCreaId = usuarioCreaId;

        return usuario;
    }

    public void desactivar() {
        if (!activo) {
            throw new BusinessRuleViolationException("User is already inactive");
        }
        this.activo = false;
    }

    public void activar() {
        if (activo) {
            throw new BusinessRuleViolationException("User is already active");
        }
        this.activo = true;
    }

    public void cambiarPassword(String newPasswordHash) {
        Objects.requireNonNull(newPasswordHash, "Password hash cannot be null");
        if (newPasswordHash.isBlank()) {
            throw new BusinessRuleViolationException("Password hash cannot be empty");
        }
        this.passwordHash = newPasswordHash;
    }

    public void actualizarPerfil(String nombreMostrar, String avatar) {
        this.nombreMostrar = nombreMostrar;
        if (avatar != null && !avatar.isBlank()) {
            this.avatar = avatar;
        }
    }

    public boolean tieneRol(String nombreRol) {
        return this.rol != null && this.rol.getNombre().equals(nombreRol);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Email getCorreo() {
        return correo;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Rol getRol() {
        return rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public Long getUsuarioCreaId() {
        return usuarioCreaId;
    }

    public String getNombreMostrar() {
        return nombreMostrar;
    }

    public String getAvatar() {
        return avatar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
