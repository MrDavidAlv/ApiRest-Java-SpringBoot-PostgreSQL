package com.litethinking.enterprise.infrastructure.persistence.adapter;

import com.litethinking.enterprise.domain.model.Usuario;
import com.litethinking.enterprise.domain.model.valueobject.Email;
import com.litethinking.enterprise.domain.port.UsuarioRepositoryPort;
import com.litethinking.enterprise.infrastructure.persistence.entity.RolEntity;
import com.litethinking.enterprise.infrastructure.persistence.entity.UsuarioEntity;
import com.litethinking.enterprise.infrastructure.persistence.mapper.UsuarioPersistenceMapper;
import com.litethinking.enterprise.infrastructure.persistence.repository.jpa.RolJpaRepository;
import com.litethinking.enterprise.infrastructure.persistence.repository.jpa.UsuarioJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository usuarioJpaRepository;
    private final RolJpaRepository rolJpaRepository;
    private final UsuarioPersistenceMapper mapper;

    public UsuarioRepositoryAdapter(
            UsuarioJpaRepository usuarioJpaRepository,
            RolJpaRepository rolJpaRepository,
            UsuarioPersistenceMapper mapper
    ) {
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.rolJpaRepository = rolJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        RolEntity rolEntity = rolJpaRepository.findById(usuario.getRol().getId())
                .orElseThrow(() -> new IllegalStateException("Role not found"));

        UsuarioEntity entity = mapper.toEntity(usuario, rolEntity);
        UsuarioEntity savedEntity = usuarioJpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Usuario> buscarPorCorreo(Email correo) {
        return usuarioJpaRepository.findByCorreo(correo.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioJpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existePorCorreo(Email correo) {
        return usuarioJpaRepository.existsByCorreo(correo.getValue());
    }

    @Override
    public void eliminar(Long id) {
        usuarioJpaRepository.deleteById(id);
    }
}
