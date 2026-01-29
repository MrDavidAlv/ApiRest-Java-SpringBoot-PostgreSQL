package com.litethinking.enterprise.infrastructure.persistence.adapter;

import com.litethinking.enterprise.application.dto.ProductoFilters;
import com.litethinking.enterprise.domain.model.Producto;
import com.litethinking.enterprise.domain.model.valueobject.CodigoProducto;
import com.litethinking.enterprise.domain.model.valueobject.Nit;
import com.litethinking.enterprise.domain.port.ProductoRepositoryPort;
import com.litethinking.enterprise.infrastructure.persistence.entity.CategoriaEntity;
import com.litethinking.enterprise.infrastructure.persistence.entity.ProductoEntity;
import com.litethinking.enterprise.infrastructure.persistence.mapper.ProductoPersistenceMapper;
import com.litethinking.enterprise.infrastructure.persistence.repository.jpa.ProductoJpaRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    public List<Producto> buscar(ProductoFilters filters) {
        Specification<ProductoEntity> spec = buildSpecification(filters);
        return jpaRepository.findAll(spec).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existePorCodigo(CodigoProducto codigo) {
        return jpaRepository.existsById(codigo.getValue());
    }

    private Specification<ProductoEntity> buildSpecification(ProductoFilters filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filters.searchTerm() != null && !filters.searchTerm().isBlank()) {
                String searchPattern = "%" + filters.searchTerm().toLowerCase() + "%";
                Predicate nombreMatch = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nombre")),
                        searchPattern
                );
                Predicate codigoMatch = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("codigo")),
                        searchPattern
                );
                predicates.add(criteriaBuilder.or(nombreMatch, codigoMatch));
            }

            if (filters.activo() != null) {
                predicates.add(criteriaBuilder.equal(root.get("activo"), filters.activo()));
            }

            if (filters.empresaNit() != null && !filters.empresaNit().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("empresaNit"), filters.empresaNit()));
            }

            if (filters.categoriaId() != null) {
                Join<ProductoEntity, CategoriaEntity> categorias = root.join("categorias");
                predicates.add(criteriaBuilder.equal(categorias.get("id"), filters.categoriaId()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public void eliminar(CodigoProducto codigo) {
        jpaRepository.deleteById(codigo.getValue());
    }
}
