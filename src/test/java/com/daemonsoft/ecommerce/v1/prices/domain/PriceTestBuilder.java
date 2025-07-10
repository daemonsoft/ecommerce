package com.daemonsoft.ecommerce.v1.prices.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PriceTestBuilder {
  private Long id = 1L;
  private Long brandId = 1L;
  private LocalDateTime validFrom = LocalDateTime.of(2023, 1, 1, 0, 0);
  private LocalDateTime validUntil = LocalDateTime.of(2023, 12, 31, 23, 59);
  private Long rateId = 1L;
  private Long productId = 35455L;
  private Integer priority = 1;
  private BigDecimal finalPrice = new BigDecimal("10.00");
  private Currency currency = Currency.EUR;

  public PriceTestBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public PriceTestBuilder withBrandId(Long brandId) {
    this.brandId = brandId;
    return this;
  }

  public PriceTestBuilder withValidFrom(LocalDateTime validFrom) {
    this.validFrom = validFrom;
    return this;
  }

  public PriceTestBuilder withValidUntil(LocalDateTime validUntil) {
    this.validUntil = validUntil;
    return this;
  }

  public PriceTestBuilder withRateId(Long rateId) {
    this.rateId = rateId;
    return this;
  }

  public PriceTestBuilder withProductId(Long productId) {
    this.productId = productId;
    return this;
  }

  public PriceTestBuilder withPriority(Integer priority) {
    this.priority = priority;
    return this;
  }

  public PriceTestBuilder withFinalPrice(BigDecimal finalPrice) {
    this.finalPrice = finalPrice;
    return this;
  }

  public PriceTestBuilder withCurrency(Currency currency) {
    this.currency = currency;
    return this;
  }

  public Price build() {
    return new Price(
        id, brandId, validFrom, validUntil, rateId, productId, priority, finalPrice, currency);
  }
}
