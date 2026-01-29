package com.litethinking.enterprise.application.usecase;

import com.litethinking.enterprise.application.dto.request.ResetPasswordRequest;
import com.litethinking.enterprise.domain.exception.BusinessRuleViolationException;
import com.litethinking.enterprise.domain.exception.ResourceNotFoundException;
import com.litethinking.enterprise.domain.model.Usuario;
import com.litethinking.enterprise.domain.port.UsuarioRepositoryPort;
import com.litethinking.enterprise.infrastructure.persistence.entity.PasswordResetTokenEntity;
import com.litethinking.enterprise.infrastructure.persistence.repository.jpa.PasswordResetTokenJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class ResetPasswordUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordUseCase.class);

    private final UsuarioRepositoryPort usuarioRepository;
    private final PasswordResetTokenJpaRepository tokenRepository;
    private final AuthenticationUseCase.PasswordEncoder passwordEncoder;

    public ResetPasswordUseCase(
            UsuarioRepositoryPort usuarioRepository,
            PasswordResetTokenJpaRepository tokenRepository,
            AuthenticationUseCase.PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void execute(ResetPasswordRequest request) {
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new BusinessRuleViolationException("New password and confirmation do not match");
        }

        PasswordResetTokenEntity tokenEntity = tokenRepository.findByToken(request.token())
                .orElseThrow(() -> new BusinessRuleViolationException("Invalid or expired reset token"));

        if (tokenEntity.getUsado()) {
            throw new BusinessRuleViolationException("This reset token has already been used");
        }

        if (tokenEntity.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            throw new BusinessRuleViolationException("This reset token has expired");
        }

        Usuario usuario = usuarioRepository.buscarPorId(tokenEntity.getUsuarioId())
                .orElseThrow(() -> ResourceNotFoundException.forResource("Usuario", tokenEntity.getUsuarioId().toString()));

        String newPasswordHash = passwordEncoder.encode(request.newPassword());
        usuario.cambiarPassword(newPasswordHash);

        usuarioRepository.guardar(usuario);

        tokenEntity.setUsado(true);
        tokenRepository.save(tokenEntity);

        logger.info("Password reset successfully for user: {}", usuario.getId());
    }
}
