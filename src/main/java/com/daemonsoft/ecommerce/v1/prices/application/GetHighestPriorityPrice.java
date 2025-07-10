package com.daemonsoft.ecommerce.v1.prices.application;

import com.daemonsoft.ecommerce.v1.prices.domain.PriceRepository;
import com.daemonsoft.ecommerce.v1.prices.domain.exception.PriceNotFoundException;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Application service for retrieving the highest priority price for a product based on brand and
 * application date.
 *
 * <p>This service implements the business use case of finding the applicable price when multiple
 * prices exist for the same product and brand, selecting the one with the highest priority that is
 * valid for the given date.
 */
@Service
public class GetHighestPriorityPrice {

  private final PriceRepository priceRepository;

  public GetHighestPriorityPrice(PriceRepository priceRepository) {
    this.priceRepository = priceRepository;
  }

  /**
   * Executes the use case to find the highest priority price for a product.
   *
   * @param productId the product ID to search for
   * @param brandId the brand ID to search for
   * @param applicationDate the date to check validity against
   * @return a Mono containing the PriceResponseDTO with the highest priority price
   * @throws PriceNotFoundException when no valid price is found
   */
  public Mono<PriceResponseDTO> execute(
      Long productId, Long brandId, LocalDateTime applicationDate) {
    return this.priceRepository
        .findPrice(brandId, productId, applicationDate)
        .switchIfEmpty(
            Mono.error(new PriceNotFoundException("Price not found for product " + productId)))
        .map(PriceMapper::toDto);
  }
}
