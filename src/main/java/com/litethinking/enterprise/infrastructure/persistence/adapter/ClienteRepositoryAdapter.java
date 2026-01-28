package com.litethinking.enterprise.infrastructure.persistence.adapter;

import com.litethinking.enterprise.domain.model.Cliente;
import com.litethinking.enterprise.domain.port.ClienteRepositoryPort;
import com.litethinking.enterprise.infrastructure.persistence.entity.ClienteEntity;
import com.litethinking.enterprise.infrastructure.persistence.mapper.ClientePersistenceMapper;
import com.litethinking.enterprise.infrastructure.persistence.repository.jpa.ClienteJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ClienteRepositoryAdapter implements ClienteRepositoryPort {

    private final ClienteJpaRepository jpaRepository;
    private final ClientePersistenceMapper mapper;

    public ClienteRepositoryAdapter(ClienteJpaRepository jpaRepository, ClientePersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        ClienteEntity entity = mapper.toEntity(cliente);
        ClienteEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Cliente> buscarPorDocumento(String documento) {
        return jpaRepository.findByDocumento(documento)
                .map(mapper::toDomain);
    }

    @Override
    public List<Cliente> buscarTodos() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existePorDocumento(String documento) {
        return jpaRepository.existsByDocumento(documento);
    }

    @Override
    public void eliminar(Long id) {
        jpaRepository.deleteById(id);
    }
}
