package com.litethinking.enterprise.infrastructure.persistence.adapter;

import com.litethinking.enterprise.domain.model.Producto;
import com.litethinking.enterprise.domain.model.valueobject.CodigoProducto;
import com.litethinking.enterprise.domain.model.valueobject.Nit;
import com.litethinking.enterprise.domain.port.ProductoRepositoryPort;
import com.litethinking.enterprise.infrastructure.persistence.entity.ProductoEntity;
import com.litethinking.enterprise.infrastructure.persistence.mapper.ProductoPersistenceMapper;
import com.litethinking.enterprise.infrastructure.persistence.repository.jpa.ProductoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductoRepositoryAdapter implements ProductoRepositoryPort {

    private final ProductoJpaRepository jpaRepository;
    private final ProductoPersistenceMapper mapper;

    public ProductoRepositoryAdapter(ProductoJpaRepository jpaRepository, ProductoPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Producto guardar(Producto producto) {
        ProductoEntity entity = mapper.toEntity(producto);
        ProductoEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Producto> buscarPorCodigo(CodigoProducto codigo) {
        return jpaRepository.findById(codigo.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public List<Producto> buscarTodos() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Producto> buscarPorEmpresa(Nit empresaNit) {
        return jpaRepository.findAllByEmpresaNit(empresaNit.getValue()).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Producto> buscarActivos() {
        return jpaRepository.findAllByActivoTrue().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existePorCodigo(CodigoProducto codigo) {
        return jpaRepository.existsById(codigo.getValue());
    }

    @Override
    public void eliminar(CodigoProducto codigo) {
        jpaRepository.deleteById(codigo.getValue());
    }
}
