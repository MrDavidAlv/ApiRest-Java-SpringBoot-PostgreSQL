package com.litethinking.enterprise.application.usecase;

import com.litethinking.enterprise.application.dto.request.LoginRequest;
import com.litethinking.enterprise.application.dto.response.LoginResponse;
import com.litethinking.enterprise.domain.exception.BusinessRuleViolationException;
import com.litethinking.enterprise.domain.exception.ResourceNotFoundException;
import com.litethinking.enterprise.domain.model.Usuario;
import com.litethinking.enterprise.domain.model.valueobject.Email;
import com.litethinking.enterprise.domain.port.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthenticationUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RecaptchaValidator recaptchaValidator;

    public AuthenticationUseCase(
            UsuarioRepositoryPort usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider,
            RecaptchaValidator recaptchaValidator
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.recaptchaValidator = recaptchaValidator;
    }

    public LoginResponse login(LoginRequest request) {
        recaptchaValidator.validate(request.recaptchaToken());

        Email correo = Email.of(request.correo());

        Usuario usuario = usuarioRepository.buscarPorCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid credentials"));

        if (!usuario.isActivo()) {
            throw new BusinessRuleViolationException("User account is inactive");
        }

        if (!passwordEncoder.matches(request.password(), usuario.getPasswordHash())) {
            throw new ResourceNotFoundException("Invalid credentials");
        }

        String token = jwtTokenProvider.generateToken(usuario);
        long expiraEn = jwtTokenProvider.getExpirationMillis();

        return LoginResponse.of(
                token,
                usuario.getId(),
                usuario.getCorreo().getValue(),
                usuario.getRol().getNombre(),
                expiraEn
        );
    }

    public interface PasswordEncoder {
        boolean matches(String rawPassword, String encodedPassword);
        String encode(String rawPassword);
    }

    public interface JwtTokenProvider {
        String generateToken(Usuario usuario);
        long getExpirationMillis();
    }

    public interface RecaptchaValidator {
        void validate(String token);
    }
}
