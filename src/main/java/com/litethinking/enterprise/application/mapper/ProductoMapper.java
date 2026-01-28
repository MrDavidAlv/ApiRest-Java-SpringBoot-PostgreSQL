package com.litethinking.enterprise.application.mapper;

import com.litethinking.enterprise.application.dto.response.ProductoResponse;
import com.litethinking.enterprise.domain.model.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    @Mapping(target = "codigo", expression = "java(producto.getCodigo().getValue())")
    @Mapping(target = "empresaNit", expression = "java(producto.getEmpresaNit().getValue())")
    @Mapping(target = "precios", ignore = true)
    ProductoResponse toResponse(Producto producto);

    List<ProductoResponse> toResponseList(List<Producto> productos);
}
