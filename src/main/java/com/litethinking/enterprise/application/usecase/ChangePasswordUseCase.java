package com.litethinking.enterprise.application.usecase;

import com.litethinking.enterprise.application.dto.request.ChangePasswordRequest;
import com.litethinking.enterprise.domain.exception.BusinessRuleViolationException;
import com.litethinking.enterprise.domain.exception.InvalidCredentialsException;
import com.litethinking.enterprise.domain.exception.ResourceNotFoundException;
import com.litethinking.enterprise.domain.model.Usuario;
import com.litethinking.enterprise.domain.port.UsuarioRepositoryPort;
import com.litethinking.enterprise.infrastructure.security.UserContextService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChangePasswordUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final UserContextService userContextService;
    private final AuthenticationUseCase.PasswordEncoder passwordEncoder;

    public ChangePasswordUseCase(
            UsuarioRepositoryPort usuarioRepository,
            UserContextService userContextService,
            AuthenticationUseCase.PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.userContextService = userContextService;
        this.passwordEncoder = passwordEncoder;
    }

    public void execute(ChangePasswordRequest request) {
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new BusinessRuleViolationException("New password and confirmation do not match");
        }

        Long currentUserId = userContextService.getCurrentUserId();
        Usuario usuario = usuarioRepository.buscarPorId(currentUserId)
                .orElseThrow(() -> ResourceNotFoundException.forResource("Usuario", currentUserId.toString()));

        if (!passwordEncoder.matches(request.currentPassword(), usuario.getPasswordHash())) {
            throw InvalidCredentialsException.withDefaultMessage();
        }

        String newPasswordHash = passwordEncoder.encode(request.newPassword());
        usuario.cambiarPassword(newPasswordHash);

        usuarioRepository.guardar(usuario);
    }
}
