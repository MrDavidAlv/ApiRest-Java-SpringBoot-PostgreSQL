package com.litethinking.enterprise.infrastructure.persistence.repository.jpa;

import com.litethinking.enterprise.infrastructure.persistence.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoJpaRepository extends JpaRepository<ProductoEntity, String> {

    List<ProductoEntity> findAllByActivoTrue();

    List<ProductoEntity> findAllByEmpresaNit(String empresaNit);
}
