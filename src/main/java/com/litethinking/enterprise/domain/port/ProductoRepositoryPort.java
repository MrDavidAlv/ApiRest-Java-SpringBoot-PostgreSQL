package com.litethinking.enterprise.domain.port;

import com.litethinking.enterprise.application.dto.ProductoFilters;
import com.litethinking.enterprise.domain.model.Producto;
import com.litethinking.enterprise.domain.model.valueobject.CodigoProducto;
import com.litethinking.enterprise.domain.model.valueobject.Nit;

import java.util.List;
import java.util.Optional;

public interface ProductoRepositoryPort {

    Producto guardar(Producto producto);

    Optional<Producto> buscarPorCodigo(CodigoProducto codigo);

    List<Producto> buscarTodos();

    List<Producto> buscarPorEmpresa(Nit empresaNit);

    List<Producto> buscarActivos();

    List<Producto> buscar(ProductoFilters filters);

    boolean existePorCodigo(CodigoProducto codigo);

    void eliminar(CodigoProducto codigo);
}
