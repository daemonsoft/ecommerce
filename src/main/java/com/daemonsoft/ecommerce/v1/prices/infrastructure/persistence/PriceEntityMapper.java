package com.daemonsoft.ecommerce.v1.prices.infrastructure.persistence;

import com.daemonsoft.ecommerce.v1.prices.domain.Currency;
import com.daemonsoft.ecommerce.v1.prices.domain.Price;

public class PriceEntityMapper {

  private PriceEntityMapper() {}

  public static Price toDomain(PriceEntity entity) {
    Currency currency = null;
    if (entity.getCurrencyCode() != null) {
      currency = Currency.valueOf(entity.getCurrencyCode());
    }
    return new Price(
        entity.getId(),
        entity.getBrandId(),
        entity.getValidFrom(),
        entity.getValidUntil(),
        entity.getRate(),
        entity.getProductId(),
        entity.getPriority(),
        entity.getFinalPrice(),
        currency);
  }
}
