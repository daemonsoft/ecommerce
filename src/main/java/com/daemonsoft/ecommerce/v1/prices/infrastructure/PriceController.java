package com.daemonsoft.ecommerce.v1.prices.infrastructure;

import com.daemonsoft.ecommerce.v1.prices.application.GetProductPriceByBrandAndDate;
import com.daemonsoft.ecommerce.v1.prices.application.PriceResponseDTO;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/prices")
public class PriceController {

  private final GetProductPriceByBrandAndDate getProductPriceByBrandAndDate;

  public PriceController(GetProductPriceByBrandAndDate getProductPriceByBrandAndDate) {
    this.getProductPriceByBrandAndDate = getProductPriceByBrandAndDate;
  }

  @GetMapping
  public Mono<ResponseEntity<PriceResponseDTO>> getPrices(
      @RequestParam Long brandId,
      @RequestParam Long productId,
      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd-HH:mm:ss")
          LocalDateTime applicationDate) {

    return this.getProductPriceByBrandAndDate
        .execute(productId, brandId, applicationDate)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }
}
