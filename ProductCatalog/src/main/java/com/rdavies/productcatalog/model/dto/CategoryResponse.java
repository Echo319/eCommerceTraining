package com.rdavies.productcatalog.model.dto;

public record CategoryResponse(
        Long id,
        String name,
        String description
) {}
