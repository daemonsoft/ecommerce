package com.daemonsoft.ecommerce.v1.prices.infrastructure.persistence;

import com.daemonsoft.ecommerce.v1.prices.domain.Currency;
import com.daemonsoft.ecommerce.v1.prices.domain.Price;
import java.util.Objects;

/** Mapper for converting between PriceTable (database) and Price (domain) objects. */
public class PriceTableMapper {

  private PriceTableMapper() {}

  /**
   * Converts a PriceTable (database mapping) to a Price (domain object).
   *
   * @param table the database table mapping
   * @return the domain Price object
   */
  public static Price toDomain(PriceTable table) {
    Currency currency = null;
    if (Objects.nonNull(table.getCurrencyCode())) {
      currency = Currency.valueOf(table.getCurrencyCode());
    }
    return new Price(
        table.getId(),
        table.getBrandId(),
        table.getValidFrom(),
        table.getValidUntil(),
        table.getRateId(),
        table.getProductId(),
        table.getPriority(),
        table.getFinalPrice(),
        currency);
  }
}
