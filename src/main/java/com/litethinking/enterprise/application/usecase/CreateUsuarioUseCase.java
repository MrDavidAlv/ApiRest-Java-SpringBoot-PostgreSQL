package com.litethinking.enterprise.application.usecase;

import com.litethinking.enterprise.application.dto.request.UsuarioCreateRequest;
import com.litethinking.enterprise.application.dto.response.UsuarioResponse;
import com.litethinking.enterprise.domain.exception.BusinessRuleViolationException;
import com.litethinking.enterprise.domain.exception.DuplicateResourceException;
import com.litethinking.enterprise.domain.model.Rol;
import com.litethinking.enterprise.domain.model.Usuario;
import com.litethinking.enterprise.domain.model.valueobject.Email;
import com.litethinking.enterprise.domain.port.UsuarioRepositoryPort;
import com.litethinking.enterprise.infrastructure.persistence.repository.jpa.RolJpaRepository;
import com.litethinking.enterprise.infrastructure.security.UserContextService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final RolJpaRepository rolRepository;
    private final UserContextService userContextService;
    private final AuthenticationUseCase.PasswordEncoder passwordEncoder;

    public CreateUsuarioUseCase(
            UsuarioRepositoryPort usuarioRepository,
            RolJpaRepository rolRepository,
            UserContextService userContextService,
            AuthenticationUseCase.PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.userContextService = userContextService;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponse execute(UsuarioCreateRequest request) {
        Email correo = Email.of(request.correo());

        if (usuarioRepository.existePorCorreo(correo)) {
            throw DuplicateResourceException.forResource("Usuario", request.correo());
        }

        var rolEntity = rolRepository.findByNombre("EXTERNO")
                .orElseThrow(() -> new BusinessRuleViolationException("EXTERNO role not found"));

        Rol rol = Rol.of(rolEntity.getId(), rolEntity.getNombre(), rolEntity.getDescripcion());

        String passwordHash = passwordEncoder.encode(request.password());
        Long currentUserId = userContextService.getCurrentUserId();

        Usuario usuario = Usuario.crear(correo, passwordHash, rol, currentUserId);

        if (request.nombreMostrar() != null || request.avatar() != null) {
            usuario.actualizarPerfil(request.nombreMostrar(), request.avatar());
        }

        Usuario usuarioGuardado = usuarioRepository.guardar(usuario);

        return new UsuarioResponse(
                usuarioGuardado.getId(),
                usuarioGuardado.getCorreo().getValue(),
                usuarioGuardado.getNombreMostrar(),
                usuarioGuardado.getAvatar(),
                usuarioGuardado.getRol().getNombre(),
                usuarioGuardado.isActivo(),
                usuarioGuardado.getFechaCreacion()
        );
    }
}
