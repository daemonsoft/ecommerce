package com.daemonsoft.ecommerce.v1.prices.application;

import static org.mockito.Mockito.when;

import com.daemonsoft.ecommerce.v1.prices.domain.Currency;
import com.daemonsoft.ecommerce.v1.prices.domain.Price;
import com.daemonsoft.ecommerce.v1.prices.domain.PriceRepository;
import com.daemonsoft.ecommerce.v1.prices.domain.exception.InvalidPriceRangeException;
import com.daemonsoft.ecommerce.v1.prices.domain.exception.PriceNotFoundException;
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
class GetHighestPriorityPriceTest {

  @Mock private PriceRepository priceRepository;

  private GetHighestPriorityPrice getHighestPriorityPrice;

  @BeforeEach
  void setup() {
    this.getHighestPriorityPrice = new GetHighestPriorityPrice(priceRepository);
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
        this.getHighestPriorityPrice.execute(productId, brandId, applicationDate);

    StepVerifier.create(result)
        .expectNextMatches(
            dto ->
                dto.brandId().equals(brandId)
                    && dto.productId().equals(productId)
                    && dto.finalPrice().compareTo(new BigDecimal("25.45")) == 0)
        .verifyComplete();
  }

  @Test
  void shouldThrowPriceNotFoundExceptionWhenPriceNotFound() {
    Long brandId = 1L;
    Long productId = 35456L;
    LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);

    when(this.priceRepository.findPrice(brandId, productId, applicationDate))
        .thenReturn(Mono.empty());

    Mono<PriceResponseDTO> result =
        this.getHighestPriorityPrice.execute(productId, brandId, applicationDate);

    StepVerifier.create(result)
        .expectErrorMatches(
            throwable ->
                throwable instanceof PriceNotFoundException
                    && throwable.getMessage().contains("Price not found"))
        .verify();
  }

  @Test
  void shouldThrowInvalidPriceRangeExceptionWhenInvalidRange() {
    Long brandId = 1L;
    Long productId = 35455L;
    LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);

    when(this.priceRepository.findPrice(brandId, productId, applicationDate))
        .thenReturn(Mono.error(new InvalidPriceRangeException("Invalid price date range")));

    Mono<PriceResponseDTO> result =
        this.getHighestPriorityPrice.execute(productId, brandId, applicationDate);

    StepVerifier.create(result)
        .expectErrorMatches(
            throwable ->
                throwable instanceof InvalidPriceRangeException
                    && throwable.getMessage().contains("Invalid price date range"))
        .verify();
  }
}
