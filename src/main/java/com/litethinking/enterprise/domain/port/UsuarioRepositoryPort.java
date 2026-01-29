package com.litethinking.enterprise.domain.port;

import com.litethinking.enterprise.domain.model.Usuario;
import com.litethinking.enterprise.domain.model.valueobject.Email;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepositoryPort {

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorCorreo(Email correo);

    Optional<Usuario> buscarPorId(Long id);

    List<Usuario> buscarTodos();

    boolean existePorCorreo(Email correo);

    void eliminar(Long id);
}
