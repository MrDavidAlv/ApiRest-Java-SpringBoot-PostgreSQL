package com.litethinking.enterprise.domain.port;

import com.litethinking.enterprise.application.dto.EmpresaFilters;
import com.litethinking.enterprise.domain.model.Empresa;
import com.litethinking.enterprise.domain.model.valueobject.Nit;

import java.util.List;
import java.util.Optional;

public interface EmpresaRepositoryPort {

    Empresa guardar(Empresa empresa);

    Optional<Empresa> buscarPorNit(Nit nit);

    List<Empresa> buscarTodas();

    List<Empresa> buscarActivas();

    List<Empresa> buscar(EmpresaFilters filters);

    boolean existePorNit(Nit nit);

    void eliminar(Nit nit);
}
