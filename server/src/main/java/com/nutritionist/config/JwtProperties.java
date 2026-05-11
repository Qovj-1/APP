package com.nutritionist.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "nutrition.jwt")
public record JwtProperties(String secret, long expireMinutes) {}
