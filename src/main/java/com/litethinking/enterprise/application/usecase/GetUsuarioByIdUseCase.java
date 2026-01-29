package com.litethinking.enterprise.application.usecase;

import com.litethinking.enterprise.application.dto.response.UsuarioResponse;
import com.litethinking.enterprise.domain.exception.ResourceNotFoundException;
import com.litethinking.enterprise.domain.model.Usuario;
import com.litethinking.enterprise.domain.port.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetUsuarioByIdUseCase {

    private final UsuarioRepositoryPort usuarioRepository;

    public GetUsuarioByIdUseCase(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioResponse execute(Long id) {
        Usuario usuario = usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> ResourceNotFoundException.forResource("Usuario", id.toString()));

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getCorreo().getValue(),
                usuario.getNombreMostrar(),
                usuario.getAvatar(),
                usuario.getRol().getNombre(),
                usuario.isActivo(),
                usuario.getFechaCreacion()
        );
    }
}
