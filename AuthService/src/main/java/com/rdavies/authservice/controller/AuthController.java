package com.rdavies.authservice.controller;

import com.rdavies.authservice.exceptions.NotUniqueException;
import com.rdavies.authservice.model.dto.AuthRequest;
import com.rdavies.authservice.model.dto.AuthResponse;
import com.rdavies.authservice.model.dto.RegisterRequest;
import com.rdavies.authservice.service.AuthService;
import com.rdavies.authservice.service.impl.AuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    public final AuthService authService;

    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {

            AuthResponse response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (NotUniqueException e) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "code", "USER_ALREADY_EXISTS",
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        try {

            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "code", "INVALID_CREDENTIAL",
                    "message", "invalid username or password"
            ));
        }
    }
}
