package com.daemonsoft.ecommerce.v1.prices.application;

import com.daemonsoft.ecommerce.v1.prices.domain.PriceRepository;
import com.daemonsoft.ecommerce.v1.prices.domain.exception.PriceNotFoundException;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetProductPriceByBrandAndDate {

  private final PriceRepository priceRepository;

  public GetProductPriceByBrandAndDate(PriceRepository priceRepository) {
    this.priceRepository = priceRepository;
  }

  public Mono<PriceResponseDTO> execute(
      Long productId, Long brandId, LocalDateTime applicationDate) {
    return this.priceRepository
        .findPrice(brandId, productId, applicationDate)
        .switchIfEmpty(
            Mono.error(new PriceNotFoundException("Price not found for product " + productId)))
        .map(PriceMapper::toDto);
  }
}
