package com.litethinking.enterprise.infrastructure.security;

import com.litethinking.enterprise.application.usecase.EmpresaUseCase;
import com.litethinking.enterprise.domain.exception.BusinessRuleViolationException;
import com.litethinking.enterprise.domain.model.valueobject.Email;
import com.litethinking.enterprise.domain.port.UsuarioRepositoryPort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserContextService implements EmpresaUseCase.UserContextProvider {

    private final UsuarioRepositoryPort usuarioRepository;

    public UserContextService(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessRuleViolationException("User not authenticated");
        }

        String correo = authentication.getName();

        return usuarioRepository.buscarPorCorreo(Email.of(correo))
                .orElseThrow(() -> new BusinessRuleViolationException("User not found"))
                .getId();
    }

    @Override
    public String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessRuleViolationException("User not authenticated");
        }

        return authentication.getAuthorities().stream()
                .filter(auth -> auth instanceof SimpleGrantedAuthority)
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .findFirst()
                .orElseThrow(() -> new BusinessRuleViolationException("User role not found"));
    }
}
