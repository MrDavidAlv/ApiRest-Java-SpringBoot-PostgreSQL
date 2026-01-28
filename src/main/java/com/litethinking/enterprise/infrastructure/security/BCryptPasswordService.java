package com.litethinking.enterprise.infrastructure.security;

import com.litethinking.enterprise.application.usecase.AuthenticationUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BCryptPasswordService implements AuthenticationUseCase.PasswordEncoder {

    private static final Logger logger = LoggerFactory.getLogger(BCryptPasswordService.class);
    private final BCryptPasswordEncoder encoder;

    public BCryptPasswordService(@Value("${application.security.bcrypt.strength:12}") int strength) {
        this.encoder = new BCryptPasswordEncoder(strength);
        logger.info("BCryptPasswordService initialized with strength: {}", strength);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        logger.debug("Checking password match - Raw length: {}, Hash length: {}", rawPassword.length(), encodedPassword != null ? encodedPassword.length() : 0);
        logger.debug("Hash starts with: {}", encodedPassword != null && encodedPassword.length() > 10 ? encodedPassword.substring(0, 10) : "null");
        boolean result = encoder.matches(rawPassword, encodedPassword);
        logger.info("Password match result: {}", result);
        return result;
    }

    @Override
    public String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }
}
