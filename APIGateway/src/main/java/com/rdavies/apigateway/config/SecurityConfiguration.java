package com.rdavies.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                            JwtAuthenticationConverter jwtAuthenticationConverter) {
        http
            .authorizeExchange(exchanges -> exchanges
                // All
                .pathMatchers("/api/v1/auth/**").permitAll()
                .pathMatchers("/actuator/**").permitAll()
                .pathMatchers("/product-catalog/v3/api-docs").permitAll()
                // Users
                .pathMatchers(HttpMethod.GET, "/api/v1/products/**", "/api/v1/categories/**")
                    .authenticated()
                // Admins
                .pathMatchers(HttpMethod.POST, "/api/v1/products/**", "/api/v1/categories/**")
                .hasRole("ADMIN")
                .pathMatchers(HttpMethod.PUT, "/api/v1/products/**", "/api/v1/categories/**")
                .hasRole("ADMIN")
                .pathMatchers(HttpMethod.DELETE, "/api/v1/products/**", "/api/v1/categories/**")
                .hasRole("ADMIN")
                .pathMatchers(HttpMethod.PATCH, "/api/v1/products/**", "/api/v1/categories/**")
                .hasRole("ADMIN")
                .anyExchange().authenticated()
            )
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .oauth2ResourceServer(oauth2 -> oauth2
                    .jwt(jwt -> jwt
                            .jwtAuthenticationConverter(
                                    new ReactiveJwtAuthenticationConverterAdapter(
                                            jwtAuthenticationConverter
                                    ))));

        return http.build();
    }
}
