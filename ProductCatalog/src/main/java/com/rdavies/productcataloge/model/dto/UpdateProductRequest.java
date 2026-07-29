package com.rdavies.productcataloge.model.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Set;

public record UpdateProductRequest(

        @Size(max = 255)
        String name,
        String description,

        @Positive
        BigDecimal price,
        Boolean isActive,
        Set<Long> categoryIds
) {
}
