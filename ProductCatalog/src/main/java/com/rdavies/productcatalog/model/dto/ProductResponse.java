package com.rdavies.productcatalog.model.dto;

import java.math.BigDecimal;
import java.time.Instant;
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
