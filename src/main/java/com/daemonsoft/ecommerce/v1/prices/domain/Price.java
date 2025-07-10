package com.daemonsoft.ecommerce.v1.prices.domain;

import com.daemonsoft.ecommerce.v1.prices.domain.exception.InvalidPriceRangeException;
import com.daemonsoft.ecommerce.v1.prices.domain.exception.InvalidPriorityException;
import com.daemonsoft.ecommerce.v1.prices.domain.exception.NegativePriceException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Price(
    Long id,
    Long brandId,
    LocalDateTime validFrom,
    LocalDateTime validUntil,
    Long rateId,
    Long productId,
    Integer priority,
    BigDecimal finalPrice,
    Currency currency) {

  public Price {
    if (validFrom.isAfter(validUntil)) {
      throw new InvalidPriceRangeException("validFrom must be before validUntil");
    }
    if (finalPrice.compareTo(BigDecimal.ZERO) < 0) {
      throw new NegativePriceException("finalPrice cannot be negative");
    }
    if (priority < 0) {
      throw new InvalidPriorityException("priority cannot be negative");
    }
  }
}
