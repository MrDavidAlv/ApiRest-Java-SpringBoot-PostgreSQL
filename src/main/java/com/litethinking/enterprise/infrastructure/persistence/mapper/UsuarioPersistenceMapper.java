package com.litethinking.enterprise.infrastructure.persistence.mapper;

import com.litethinking.enterprise.domain.model.Rol;
import com.litethinking.enterprise.domain.model.Usuario;
import com.litethinking.enterprise.domain.model.valueobject.Email;
import com.litethinking.enterprise.infrastructure.persistence.entity.RolEntity;
import com.litethinking.enterprise.infrastructure.persistence.entity.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioPersistenceMapper {

    public Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) {
            return null;
        }

        Rol rol = Rol.of(
                entity.getRol().getId(),
                entity.getRol().getNombre(),
                entity.getRol().getDescripcion()
        );

        Usuario usuario = Usuario.crear(
                Email.of(entity.getCorreo()),
                entity.getPassword(),
                rol,
                entity.getUsuarioCreaId()
        );

        setId(usuario, entity.getId());

        if (entity.getNombreMostrar() != null || entity.getAvatar() != null) {
            usuario.actualizarPerfil(entity.getNombreMostrar(), entity.getAvatar());
        }

        if (!entity.getActivo()) {
            usuario.desactivar();
        }

        return usuario;
    }

    public UsuarioEntity toEntity(Usuario usuario, RolEntity rolEntity) {
        if (usuario == null) {
            return null;
        }

        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(usuario.getId());
        entity.setCorreo(usuario.getCorreo().getValue());
        entity.setPassword(usuario.getPasswordHash());
        entity.setRol(rolEntity);
        entity.setActivo(usuario.isActivo());
        entity.setNombreMostrar(usuario.getNombreMostrar());
        entity.setAvatar(usuario.getAvatar());
        entity.setFechaCreacion(usuario.getFechaCreacion());
        entity.setUsuarioCreaId(usuario.getUsuarioCreaId());

        return entity;
    }

    private void setId(Usuario usuario, Long id) {
        usuario.setId(id);
    }
}
