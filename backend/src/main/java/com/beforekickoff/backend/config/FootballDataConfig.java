package com.beforekickoff.backend.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FootballDataProperties.class)
public class FootballDataConfig {
}