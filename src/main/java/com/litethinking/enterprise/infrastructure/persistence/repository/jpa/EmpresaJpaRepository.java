package com.litethinking.enterprise.infrastructure.persistence.repository.jpa;

import com.litethinking.enterprise.infrastructure.persistence.entity.EmpresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaJpaRepository extends JpaRepository<EmpresaEntity, String>, JpaSpecificationExecutor<EmpresaEntity> {

    List<EmpresaEntity> findAllByActivoTrue();
}
