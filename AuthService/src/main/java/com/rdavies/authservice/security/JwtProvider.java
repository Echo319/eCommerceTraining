package com.rdavies.authservice.security;

import com.rdavies.authservice.model.dao.Role;
import com.rdavies.authservice.model.dao.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    private final SecretKey key;
    private final Long expirationMs;

    public JwtProvider(
            @Value("${}") String secret,
            @Value("${}") long expirationMs) {
     this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
     this.expirationMs = expirationMs;
    }

    public String generateToken(User user) {

        Date now = new Date();
        Date expiaryDate = new Date(now.getTime() + expirationMs);

        String rolesClaim = user.getRoles().stream().map(Role::getName).collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(user.getUserName())
                .claims(Map.of(
                        "userId", user.getId(),
                        "email", user.getEmail(),
                        "roles", rolesClaim
                )).issuedAt(now)
                .expiration(expiaryDate)
                .signWith(key)
                .compact();

    }


    public boolean validateToken(String token) {
        try{
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // log?
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public long getExpirationMs() {
        return expirationMs;
    }
}
