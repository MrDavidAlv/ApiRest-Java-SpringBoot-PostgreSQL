package com.litethinking.enterprise.infrastructure.persistence.mapper;

import com.litethinking.enterprise.domain.model.Empresa;
import com.litethinking.enterprise.domain.model.valueobject.Nit;
import com.litethinking.enterprise.infrastructure.persistence.entity.EmpresaEntity;
import org.springframework.stereotype.Component;

@Component
public class EmpresaPersistenceMapper {

    public Empresa toDomain(EmpresaEntity entity) {
        if (entity == null) {
            return null;
        }

        Empresa empresa = Empresa.crear(
                Nit.of(entity.getNit()),
                entity.getNombre(),
                entity.getDireccion(),
                entity.getTelefono(),
                entity.getUsuarioCreaId()
        );

        if (!entity.getActivo()) {
            empresa.desactivar();
        }

        return empresa;
    }

    public EmpresaEntity toEntity(Empresa empresa) {
        if (empresa == null) {
            return null;
        }

        EmpresaEntity entity = new EmpresaEntity();
        entity.setNit(empresa.getNit().getValue());
        entity.setNombre(empresa.getNombre());
        entity.setDireccion(empresa.getDireccion());
        entity.setTelefono(empresa.getTelefono());
        entity.setActivo(empresa.isActivo());
        entity.setFechaCreacion(empresa.getFechaCreacion());
        entity.setFechaModificacion(empresa.getFechaModificacion());
        entity.setUsuarioCreaId(empresa.getUsuarioCreaId());
        entity.setUsuarioModificaId(empresa.getUsuarioModificaId());

        return entity;
    }
}
