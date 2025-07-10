package com.daemonsoft.ecommerce.v1.prices.infrastructure.persistence;

import java.time.LocalDateTime;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for Price table mappings. Provides reactive database operations for
 * price data.
 */
public interface PriceSpringDataRepository extends ReactiveCrudRepository<PriceTable, Long> {

  /**
   * Finds the highest priority price for a specific product and brand at a given date.
   *
   * @param productId the product ID to search for
   * @param brandId the brand ID to search for
   * @param applicationDate the date to check validity against
   * @return a Mono containing the matching PriceTable with highest priority
   */
  @Query(
      """
      SELECT * FROM prices
      WHERE product_id = :productId
        AND brand_id = :brandId
        AND :applicationDate BETWEEN valid_from AND valid_until
      ORDER BY priority DESC
      LIMIT 1
      """)
  Mono<PriceTable> findHighestPriorityPrice(
      Long productId, Long brandId, LocalDateTime applicationDate);
}
