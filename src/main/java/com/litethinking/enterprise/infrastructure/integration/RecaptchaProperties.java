package com.litethinking.enterprise.infrastructure.integration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.recaptcha")
@Getter
@Setter
public class RecaptchaProperties {

    private String secretKey;
    private String verifyUrl;
    private Double threshold;
}
