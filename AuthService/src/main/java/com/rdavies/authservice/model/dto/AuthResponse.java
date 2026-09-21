package com.rdavies.authservice.model.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthResponse (
        String token,
        String tokenType,
        long expiresInMs,
        String username,
        String role
){
}
