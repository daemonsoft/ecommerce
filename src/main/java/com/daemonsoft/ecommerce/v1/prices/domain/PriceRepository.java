package com.daemonsoft.ecommerce.v1.prices.domain;

import java.time.LocalDateTime;
import reactor.core.publisher.Mono;

public interface PriceRepository {

  /**
   * Retrieves the price for a product based on brand and date.
   *
   * @param brandId the ID of the brand
   * @param productId the ID of the product
   * @param date the date for which the price is requested
   * @return a Mono containing the Price if found, or empty if not found
   */
  Mono<Price> findPrice(Long brandId, Long productId, LocalDateTime date);
}
