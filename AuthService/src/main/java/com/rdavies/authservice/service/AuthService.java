package com.rdavies.authservice.service;

import com.rdavies.authservice.model.dto.AuthRequest;
import com.rdavies.authservice.model.dto.AuthResponse;
import com.rdavies.authservice.model.dto.RegisterRequest;

public interface AuthService
{
    AuthResponse register(RegisterRequest request);

    AuthResponse login(AuthRequest request);
}
