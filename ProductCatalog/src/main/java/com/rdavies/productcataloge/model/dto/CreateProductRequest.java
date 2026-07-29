package com.rdavies.productcataloge.model.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Set;

public record CreateProductRequest(

        @NotBlank
        @Size(max = 100)
        String sku,

        @NotBlank
        @Size(max = 255)
        String name,

        String description,

        @NotNull
        @Positive
        BigDecimal price,

        @NotEmpty
        Set<Integer> categoryIds

) {
}
