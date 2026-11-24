package com.beforekickoff.backend.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "ml")
public record MlProperties(

        @NotBlank
        String baseUrl,

        @Min(1)
        int connectTimeoutMs,

        @Min(1)
        int readTimeoutMs
) {
}