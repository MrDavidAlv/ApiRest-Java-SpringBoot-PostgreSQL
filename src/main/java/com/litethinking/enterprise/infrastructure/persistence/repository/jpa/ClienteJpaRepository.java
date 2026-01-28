package com.litethinking.enterprise.infrastructure.persistence.repository.jpa;

import com.litethinking.enterprise.infrastructure.persistence.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteJpaRepository extends JpaRepository<ClienteEntity, Long> {

    Optional<ClienteEntity> findByDocumento(String documento);

    boolean existsByDocumento(String documento);
}
