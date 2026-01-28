package com.litethinking.enterprise.infrastructure.persistence.mapper;

import com.litethinking.enterprise.domain.model.EstadoOrden;
import com.litethinking.enterprise.domain.model.Orden;
import com.litethinking.enterprise.domain.model.OrdenDetalle;
import com.litethinking.enterprise.domain.model.valueobject.CodigoProducto;
import com.litethinking.enterprise.domain.model.valueobject.Money;
import com.litethinking.enterprise.infrastructure.persistence.entity.EstadoOrdenEntity;
import com.litethinking.enterprise.infrastructure.persistence.entity.OrdenDetalleEntity;
import com.litethinking.enterprise.infrastructure.persistence.entity.OrdenEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrdenPersistenceMapper {

    public Orden toDomain(OrdenEntity entity) {
        if (entity == null) {
            return null;
        }

        EstadoOrden estadoOrden = EstadoOrden.of(
                entity.getEstado().getId(),
                entity.getEstado().getNombre()
        );

        Orden orden = Orden.crear(
                entity.getClienteId(),
                estadoOrden,
                entity.getUsuarioCreaId()
        );

        setId(orden, entity.getId());

        entity.getDetalles().forEach(detalleEntity -> {
            OrdenDetalle detalle = ordenDetalleToDomain(detalleEntity);
            setDetalleId(detalle, detalleEntity.getId());
        });

        return orden;
    }

    public OrdenEntity toEntity(Orden orden, EstadoOrdenEntity estadoEntity) {
        if (orden == null) {
            return null;
        }

        OrdenEntity entity = new OrdenEntity();
        entity.setId(orden.getId());
        entity.setClienteId(orden.getClienteId());
        entity.setEstado(estadoEntity);
        entity.setFechaOrden(orden.getFechaOrden());
        entity.setUsuarioCreaId(orden.getUsuarioCreaId());

        orden.getDetalles().forEach(detalle -> {
            OrdenDetalleEntity detalleEntity = ordenDetalleToEntity(detalle);
            entity.addDetalle(detalleEntity);
        });

        return entity;
    }

    public OrdenDetalle ordenDetalleToDomain(OrdenDetalleEntity entity) {
        Money precioUnitario = Money.of(entity.getPrecioUnitarioHistorico(), "COP");

        OrdenDetalle detalle = OrdenDetalle.crear(
                CodigoProducto.of(entity.getProductoCodigo()),
                entity.getCantidad(),
                precioUnitario
        );

        return detalle;
    }

    public OrdenDetalleEntity ordenDetalleToEntity(OrdenDetalle detalle) {
        OrdenDetalleEntity entity = new OrdenDetalleEntity();
        entity.setId(detalle.getId());
        entity.setProductoCodigo(detalle.getProductoCodigo().getValue());
        entity.setCantidad(detalle.getCantidad());
        entity.setPrecioUnitarioHistorico(detalle.getPrecioUnitarioHistorico().getAmount());

        return entity;
    }

    private void setId(Orden orden, Long id) {
        orden.setId(id);
    }

    private void setDetalleId(OrdenDetalle detalle, Long id) {
        detalle.setId(id);
    }
}
