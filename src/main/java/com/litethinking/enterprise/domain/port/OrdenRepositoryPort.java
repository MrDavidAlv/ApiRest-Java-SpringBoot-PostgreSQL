package com.litethinking.enterprise.domain.port;

import com.litethinking.enterprise.domain.model.Orden;

import java.util.List;
import java.util.Optional;

public interface OrdenRepositoryPort {

    Orden guardar(Orden orden);

    Optional<Orden> buscarPorId(Long id);

    List<Orden> buscarTodas();

    List<Orden> buscarPorCliente(Long clienteId);

    List<Orden> buscarPorEstado(Integer estadoId);

    void eliminar(Long id);
}
