package com.daemonsoft.ecommerce.v1.prices.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Price(
    Long id,
    Long brandId,
    LocalDateTime validFrom,
    LocalDateTime validUntil,
    Long rate,
    Long productId,
    Integer priority,
    BigDecimal finalPrice,
    Currency currency) {}
