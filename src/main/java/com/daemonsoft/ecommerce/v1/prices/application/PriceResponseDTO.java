package com.daemonsoft.ecommerce.v1.prices.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceResponseDTO(
    Long productId,
    Long brandId,
    Long rate,
    LocalDateTime validFrom,
    LocalDateTime validUntil,
    BigDecimal finalPrice) {}
