package com.rdavies.productcataloge.model.dto;

public record CategoryResponse(
        Long id,
        String name,
        String description
) {}
