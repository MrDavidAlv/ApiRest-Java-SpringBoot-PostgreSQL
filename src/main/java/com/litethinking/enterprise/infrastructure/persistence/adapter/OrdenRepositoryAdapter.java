package com.litethinking.enterprise.infrastructure.persistence.adapter;

import com.litethinking.enterprise.domain.model.Orden;
import com.litethinking.enterprise.domain.port.OrdenRepositoryPort;
import com.litethinking.enterprise.infrastructure.persistence.entity.EstadoOrdenEntity;
import com.litethinking.enterprise.infrastructure.persistence.entity.OrdenEntity;
import com.litethinking.enterprise.infrastructure.persistence.mapper.OrdenPersistenceMapper;
import com.litethinking.enterprise.infrastructure.persistence.repository.jpa.EstadoOrdenJpaRepository;
import com.litethinking.enterprise.infrastructure.persistence.repository.jpa.OrdenJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrdenRepositoryAdapter implements OrdenRepositoryPort {

    private final OrdenJpaRepository ordenJpaRepository;
    private final EstadoOrdenJpaRepository estadoOrdenJpaRepository;
    private final OrdenPersistenceMapper mapper;

    public OrdenRepositoryAdapter(
            OrdenJpaRepository ordenJpaRepository,
            EstadoOrdenJpaRepository estadoOrdenJpaRepository,
            OrdenPersistenceMapper mapper
    ) {
        this.ordenJpaRepository = ordenJpaRepository;
        this.estadoOrdenJpaRepository = estadoOrdenJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Orden guardar(Orden orden) {
        EstadoOrdenEntity estadoEntity = estadoOrdenJpaRepository.findById(orden.getEstado().getId())
                .orElseThrow(() -> new IllegalStateException("Estado not found"));

        OrdenEntity entity = mapper.toEntity(orden, estadoEntity);
        OrdenEntity savedEntity = ordenJpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Orden> buscarPorId(Long id) {
        return ordenJpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Orden> buscarTodas() {
        return ordenJpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Orden> buscarPorCliente(Long clienteId) {
        return ordenJpaRepository.findAllByClienteId(clienteId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Orden> buscarPorEstado(Integer estadoId) {
        return ordenJpaRepository.findAllByEstadoId(estadoId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void eliminar(Long id) {
        ordenJpaRepository.deleteById(id);
    }
}
