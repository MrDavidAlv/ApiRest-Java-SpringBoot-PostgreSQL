package com.litethinking.enterprise.application.usecase;

import com.litethinking.enterprise.application.dto.response.UsuarioResponse;
import com.litethinking.enterprise.domain.exception.ResourceNotFoundException;
import com.litethinking.enterprise.domain.model.Usuario;
import com.litethinking.enterprise.domain.port.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ToggleUsuarioStatusUseCase {

    private final UsuarioRepositoryPort usuarioRepository;

    public ToggleUsuarioStatusUseCase(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioResponse execute(Long usuarioId) {
        Usuario usuario = usuarioRepository.buscarPorId(usuarioId)
                .orElseThrow(() -> ResourceNotFoundException.forResource("Usuario", usuarioId.toString()));

        if (usuario.isActivo()) {
            usuario.desactivar();
        } else {
            usuario.activar();
        }

        Usuario usuarioActualizado = usuarioRepository.guardar(usuario);

        return new UsuarioResponse(
                usuarioActualizado.getId(),
                usuarioActualizado.getCorreo().getValue(),
                usuarioActualizado.getNombreMostrar(),
                usuarioActualizado.getAvatar(),
                usuarioActualizado.getRol().getNombre(),
                usuarioActualizado.isActivo(),
                usuarioActualizado.getFechaCreacion()
        );
    }
}
