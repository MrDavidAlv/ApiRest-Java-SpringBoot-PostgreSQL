package com.litethinking.enterprise.application.usecase;

import com.litethinking.enterprise.application.dto.request.ForgotPasswordRequest;
import com.litethinking.enterprise.domain.model.Usuario;
import com.litethinking.enterprise.domain.model.valueobject.Email;
import com.litethinking.enterprise.domain.port.UsuarioRepositoryPort;
import com.litethinking.enterprise.infrastructure.persistence.entity.PasswordResetTokenEntity;
import com.litethinking.enterprise.infrastructure.persistence.repository.jpa.PasswordResetTokenJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class ForgotPasswordUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordUseCase.class);
    private static final int TOKEN_EXPIRATION_HOURS = 1;

    private final UsuarioRepositoryPort usuarioRepository;
    private final PasswordResetTokenJpaRepository tokenRepository;

    public ForgotPasswordUseCase(
            UsuarioRepositoryPort usuarioRepository,
            PasswordResetTokenJpaRepository tokenRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.tokenRepository = tokenRepository;
    }

    public String execute(ForgotPasswordRequest request) {
        Email correo = Email.of(request.correo());

        var usuarioOpt = usuarioRepository.buscarPorCorreo(correo);

        if (usuarioOpt.isEmpty()) {
            logger.info("Password reset requested for non-existent email: {}", request.correo());
            return null;
        }

        Usuario usuario = usuarioOpt.get();

        tokenRepository.deleteByUsuarioIdAndUsadoFalseAndFechaExpiracionBefore(
                usuario.getId(),
                LocalDateTime.now()
        );

        String token = UUID.randomUUID().toString();
        LocalDateTime expiracion = LocalDateTime.now().plusHours(TOKEN_EXPIRATION_HOURS);

        PasswordResetTokenEntity tokenEntity = new PasswordResetTokenEntity();
        tokenEntity.setUsuarioId(usuario.getId());
        tokenEntity.setToken(token);
        tokenEntity.setFechaExpiracion(expiracion);
        tokenEntity.setUsado(false);

        tokenRepository.save(tokenEntity);

        logger.info("Password reset token generated for user: {}", usuario.getId());

        return token;
    }
}
