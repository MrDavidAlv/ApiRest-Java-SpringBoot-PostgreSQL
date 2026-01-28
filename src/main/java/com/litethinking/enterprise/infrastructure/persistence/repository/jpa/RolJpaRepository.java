package com.litethinking.enterprise.infrastructure.persistence.repository.jpa;

import com.litethinking.enterprise.infrastructure.persistence.entity.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolJpaRepository extends JpaRepository<RolEntity, Integer> {

    Optional<RolEntity> findByNombre(String nombre);
}
