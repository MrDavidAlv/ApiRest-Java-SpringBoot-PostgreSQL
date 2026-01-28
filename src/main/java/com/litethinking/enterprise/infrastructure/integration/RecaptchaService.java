package com.litethinking.enterprise.infrastructure.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.litethinking.enterprise.application.usecase.AuthenticationUseCase;
import com.litethinking.enterprise.domain.exception.BusinessRuleViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class RecaptchaService implements AuthenticationUseCase.RecaptchaValidator {

    private static final Logger logger = LoggerFactory.getLogger(RecaptchaService.class);

    private final RecaptchaProperties properties;
    private final WebClient webClient;

    public RecaptchaService(RecaptchaProperties properties) {
        this.properties = properties;
        this.webClient = WebClient.builder().build();
    }

    @Override
    public void validate(String token) {
        if (token == null || token.isBlank()) {
            throw new BusinessRuleViolationException("reCAPTCHA token is required");
        }

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("secret", properties.getSecretKey());
        formData.add("response", token);

        try {
            RecaptchaResponse response = webClient.post()
                    .uri(properties.getVerifyUrl())
                    .bodyValue(formData)
                    .retrieve()
                    .bodyToMono(RecaptchaResponse.class)
                    .block();

            if (response == null || !response.success()) {
                logger.warn("reCAPTCHA validation failed: {}", response);
                throw new BusinessRuleViolationException("reCAPTCHA validation failed");
            }

            if (response.score() < properties.getThreshold()) {
                logger.warn("reCAPTCHA score too low: {}", response.score());
                throw new BusinessRuleViolationException("reCAPTCHA score too low");
            }

        } catch (Exception e) {
            logger.error("Error validating reCAPTCHA", e);
            throw new BusinessRuleViolationException("reCAPTCHA validation error");
        }
    }

    private record RecaptchaResponse(
            boolean success,
            Double score,
            String action,
            @JsonProperty("challenge_ts") String challengeTs,
            String hostname,
            @JsonProperty("error-codes") String[] errorCodes
    ) {
    }
}
