package com.litethinking.enterprise.infrastructure.security;

import com.litethinking.enterprise.application.usecase.AuthenticationUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BCryptPasswordService implements AuthenticationUseCase.PasswordEncoder {

    private final BCryptPasswordEncoder encoder;

    public BCryptPasswordService(@Value("${application.security.bcrypt.strength:12}") int strength) {
        this.encoder = new BCryptPasswordEncoder(strength);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }
}
