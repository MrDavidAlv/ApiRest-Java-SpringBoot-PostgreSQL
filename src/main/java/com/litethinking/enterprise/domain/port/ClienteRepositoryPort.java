package com.litethinking.enterprise.domain.port;

import com.litethinking.enterprise.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepositoryPort {

    Cliente guardar(Cliente cliente);

    Optional<Cliente> buscarPorId(Long id);

    Optional<Cliente> buscarPorDocumento(String documento);

    List<Cliente> buscarTodos();

    boolean existePorDocumento(String documento);

    void eliminar(Long id);
}
