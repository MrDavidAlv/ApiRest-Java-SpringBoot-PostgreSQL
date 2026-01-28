package com.litethinking.enterprise.domain.port;

import com.litethinking.enterprise.domain.model.Usuario;
import com.litethinking.enterprise.domain.model.valueobject.Email;

import java.util.Optional;

public interface UsuarioRepositoryPort {

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorCorreo(Email correo);

    Optional<Usuario> buscarPorId(Long id);

    boolean existePorCorreo(Email correo);

    void eliminar(Long id);
}
