package com.daemonsoft.ecommerce.v1.prices.application;

import com.daemonsoft.ecommerce.v1.prices.domain.Price;

public class PriceMapper {

  private PriceMapper() {}

  public static PriceResponseDTO toDto(Price price) {
    return new PriceResponseDTO(
        price.productId(),
        price.brandId(),
        price.rateId(),
        price.validFrom(),
        price.validUntil(),
        price.finalPrice());
  }
}
