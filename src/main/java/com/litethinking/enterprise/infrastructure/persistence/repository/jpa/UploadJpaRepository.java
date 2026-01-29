package com.litethinking.enterprise.infrastructure.persistence.repository.jpa;

import com.litethinking.enterprise.infrastructure.persistence.entity.UploadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UploadJpaRepository extends JpaRepository<UploadEntity, Long> {

    Optional<UploadEntity> findByNombreArchivo(String nombreArchivo);

    void deleteByNombreArchivo(String nombreArchivo);
}
