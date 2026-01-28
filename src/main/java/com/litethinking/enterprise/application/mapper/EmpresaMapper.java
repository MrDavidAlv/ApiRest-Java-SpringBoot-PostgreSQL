package com.litethinking.enterprise.application.mapper;

import com.litethinking.enterprise.application.dto.response.EmpresaResponse;
import com.litethinking.enterprise.domain.model.Empresa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmpresaMapper {

    @Mapping(target = "nit", expression = "java(empresa.getNit().getValue())")
    EmpresaResponse toResponse(Empresa empresa);

    List<EmpresaResponse> toResponseList(List<Empresa> empresas);
}
