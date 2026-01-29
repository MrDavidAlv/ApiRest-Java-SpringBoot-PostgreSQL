package com.litethinking.enterprise.application.usecase;

import com.litethinking.enterprise.application.dto.response.UsuarioResponse;
import com.litethinking.enterprise.domain.port.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ListUsuariosUseCase {

    private final UsuarioRepositoryPort usuarioRepository;

    public ListUsuariosUseCase(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<UsuarioResponse> execute() {
        return usuarioRepository.buscarTodos().stream()
                .map(usuario -> new UsuarioResponse(
                        usuario.getId(),
                        usuario.getCorreo().getValue(),
                        usuario.getNombreMostrar(),
                        usuario.getAvatar(),
                        usuario.getRol().getNombre(),
                        usuario.isActivo(),
                        usuario.getFechaCreacion()
                ))
                .toList();
    }
}
