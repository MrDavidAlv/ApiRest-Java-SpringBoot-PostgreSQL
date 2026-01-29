package com.litethinking.enterprise.infrastructure.persistence.mapper;

import com.litethinking.enterprise.domain.model.Producto;
import com.litethinking.enterprise.domain.model.valueobject.CodigoProducto;
import com.litethinking.enterprise.domain.model.valueobject.Money;
import com.litethinking.enterprise.domain.model.valueobject.Nit;
import com.litethinking.enterprise.infrastructure.persistence.entity.ProductoEntity;
import com.litethinking.enterprise.infrastructure.persistence.entity.ProductoPrecioEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductoPersistenceMapper {

    public Producto toDomain(ProductoEntity entity) {
        if (entity == null) {
            return null;
        }

        Producto producto = Producto.crear(
                CodigoProducto.of(entity.getCodigo()),
                entity.getNombre(),
                entity.getCaracteristicas(),
                Nit.of(entity.getEmpresaNit()),
                entity.getUsuarioCreaId()
        );

        entity.getPrecios().forEach(precioEntity -> {
            Money precio = Money.of(precioEntity.getPrecio(), getCodigoMoneda(precioEntity.getMonedaId()));
            producto.agregarPrecio(precioEntity.getMonedaId(), precio);
        });

        entity.getCategorias().forEach(categoriaEntity -> {
            producto.agregarCategoria(categoriaEntity.getId());
        });

        if (entity.getUrlImagen() != null) {
            producto.actualizarImagen(entity.getUrlImagen());
        }

        if (!entity.getActivo()) {
            producto.desactivar();
        }

        return producto;
    }

    public ProductoEntity toEntity(Producto producto) {
        if (producto == null) {
            return null;
        }

        ProductoEntity entity = new ProductoEntity();
        entity.setCodigo(producto.getCodigo().getValue());
        entity.setNombre(producto.getNombre());
        entity.setCaracteristicas(producto.getCaracteristicas());
        entity.setEmpresaNit(producto.getEmpresaNit().getValue());
        entity.setActivo(producto.isActivo());
        entity.setUrlImagen(producto.getUrlImagen());
        entity.setFechaCreacion(producto.getFechaCreacion());
        entity.setFechaModificacion(producto.getFechaModificacion());
        entity.setUsuarioCreaId(producto.getUsuarioCreaId());

        producto.getPrecios().forEach((monedaId, money) -> {
            ProductoPrecioEntity precioEntity = new ProductoPrecioEntity();
            precioEntity.setMonedaId(monedaId);
            precioEntity.setPrecio(money.getAmount());
            entity.addPrecio(precioEntity);
        });

        return entity;
    }

    private String getCodigoMoneda(Integer monedaId) {
        return switch (monedaId) {
            case 1 -> "COP";
            case 2 -> "USD";
            case 3 -> "EUR";
            default -> "USD";
        };
    }
}
