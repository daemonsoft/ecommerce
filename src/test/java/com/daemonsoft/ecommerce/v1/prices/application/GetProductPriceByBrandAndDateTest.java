package com.daemonsoft.ecommerce.v1.prices.application;

import static org.mockito.Mockito.when;

import com.daemonsoft.ecommerce.v1.prices.domain.Currency;
import com.daemonsoft.ecommerce.v1.prices.domain.Price;
import com.daemonsoft.ecommerce.v1.prices.domain.PriceRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class GetProductPriceByBrandAndDateTest {

  @Mock private PriceRepository priceRepository;

  private GetProductPriceByBrandAndDate getProductPriceByBrandAndDate;

  @BeforeEach
  void setup() {
    this.getProductPriceByBrandAndDate = new GetProductPriceByBrandAndDate(priceRepository);
  }

  @Test
  void shouldReturnPriceResponseDTOWhenPriceFound() {
    Long brandId = 1L;
    Long productId = 35455L;
    LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);

    Price price =
        new Price(
            1L,
            brandId,
            LocalDateTime.of(2020, 6, 14, 0, 0),
            LocalDateTime.of(2020, 6, 15, 0, 0),
            2L,
            productId,
            1,
            new BigDecimal("25.45"),
            Currency.EUR);

    when(this.priceRepository.findPrice(brandId, productId, applicationDate))
        .thenReturn(Mono.just(price));

    Mono<PriceResponseDTO> result =
        this.getProductPriceByBrandAndDate.execute(productId, brandId, applicationDate);

    StepVerifier.create(result)
        .expectNextMatches(
            dto ->
                dto.brandId().equals(brandId)
                    && dto.productId().equals(productId)
                    && dto.finalPrice().compareTo(new BigDecimal("25.45")) == 0)
        .verifyComplete();
  }

  @Test
  void shouldReturnEmptyWhenPriceNotFound() {
    Long brandId = 1L;
    Long productId = 35455L;
    LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);

    when(this.priceRepository.findPrice(brandId, productId, applicationDate))
        .thenReturn(Mono.empty());

    Mono<PriceResponseDTO> result =
        this.getProductPriceByBrandAndDate.execute(productId, brandId, applicationDate);

    StepVerifier.create(result).verifyComplete();
  }
}
