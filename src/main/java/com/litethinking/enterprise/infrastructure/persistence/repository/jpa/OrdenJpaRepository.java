package com.litethinking.enterprise.infrastructure.persistence.repository.jpa;

import com.litethinking.enterprise.infrastructure.persistence.entity.OrdenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenJpaRepository extends JpaRepository<OrdenEntity, Long> {

    List<OrdenEntity> findAllByClienteId(Long clienteId);

    List<OrdenEntity> findAllByEstadoId(Integer estadoId);
}
