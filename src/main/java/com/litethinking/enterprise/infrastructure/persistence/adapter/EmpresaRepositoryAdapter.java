package com.litethinking.enterprise.infrastructure.persistence.adapter;

import com.litethinking.enterprise.domain.model.Empresa;
import com.litethinking.enterprise.domain.model.valueobject.Nit;
import com.litethinking.enterprise.domain.port.EmpresaRepositoryPort;
import com.litethinking.enterprise.infrastructure.persistence.entity.EmpresaEntity;
import com.litethinking.enterprise.infrastructure.persistence.mapper.EmpresaPersistenceMapper;
import com.litethinking.enterprise.infrastructure.persistence.repository.jpa.EmpresaJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EmpresaRepositoryAdapter implements EmpresaRepositoryPort {

    private final EmpresaJpaRepository jpaRepository;
    private final EmpresaPersistenceMapper mapper;

    public EmpresaRepositoryAdapter(EmpresaJpaRepository jpaRepository, EmpresaPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Empresa guardar(Empresa empresa) {
        EmpresaEntity entity = mapper.toEntity(empresa);
        EmpresaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Empresa> buscarPorNit(Nit nit) {
        return jpaRepository.findById(nit.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public List<Empresa> buscarTodas() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Empresa> buscarActivas() {
        return jpaRepository.findAllByActivoTrue().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existePorNit(Nit nit) {
        return jpaRepository.existsById(nit.getValue());
    }

    @Override
    public void eliminar(Nit nit) {
        jpaRepository.deleteById(nit.getValue());
    }
}
