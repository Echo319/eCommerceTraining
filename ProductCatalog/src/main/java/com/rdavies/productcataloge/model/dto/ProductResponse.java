package com.rdavies.productcataloge.model.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;

public record ProductResponse(
    Long id,
    String sku,
    String name,
    String description,
    BigDecimal price,
    Boolean isActive,
    Set<CategoryResponse> categories,
    Instant createdAt,
    Instant updatedAt
) {}
