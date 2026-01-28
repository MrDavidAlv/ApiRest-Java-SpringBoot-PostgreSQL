package com.litethinking.enterprise.infrastructure.persistence.mapper;

import com.litethinking.enterprise.domain.model.Cliente;
import com.litethinking.enterprise.domain.model.valueobject.Email;
import com.litethinking.enterprise.infrastructure.persistence.entity.ClienteEntity;
import org.springframework.stereotype.Component;

@Component
public class ClientePersistenceMapper {

    public Cliente toDomain(ClienteEntity entity) {
        if (entity == null) {
            return null;
        }

        Email correo = entity.getCorreo() != null && !entity.getCorreo().isBlank()
                ? Email.of(entity.getCorreo())
                : null;

        Cliente cliente = Cliente.crear(
                entity.getDocumento(),
                entity.getNombre(),
                correo
        );

        setId(cliente, entity.getId());

        return cliente;
    }

    public ClienteEntity toEntity(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        ClienteEntity entity = new ClienteEntity();
        entity.setId(cliente.getId());
        entity.setDocumento(cliente.getDocumento());
        entity.setNombre(cliente.getNombre());
        entity.setCorreo(cliente.getCorreo() != null ? cliente.getCorreo().getValue() : null);

        return entity;
    }

    private void setId(Cliente cliente, Long id) {
        cliente.setId(id);
    }
}
