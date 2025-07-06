package com.daemonsoft.ecommerce.v1.prices.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.daemonsoft.ecommerce.v1.prices.domain.exception.InvalidPriceRangeException;
import com.daemonsoft.ecommerce.v1.prices.domain.exception.InvalidPriorityException;
import com.daemonsoft.ecommerce.v1.prices.domain.exception.NegativePriceException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class PriceTest {

  @Test
  void shouldThrowInvalidPriceRangeExceptionWhenValidFromAfterValidUntil() {
    LocalDateTime validFrom = LocalDateTime.of(2020, 6, 15, 0, 0);
    LocalDateTime validUntil = LocalDateTime.of(2020, 6, 14, 0, 0);

    PriceTestBuilder builder =
        new PriceTestBuilder().withValidFrom(validFrom).withValidUntil(validUntil);

    assertThrows(InvalidPriceRangeException.class, builder::build);
  }

  @Test
  void shouldThrowNegativePriceExceptionWhenFinalPriceIsNegative() {
    PriceTestBuilder builder = new PriceTestBuilder().withFinalPrice(new BigDecimal("-1.00"));

    assertThrows(NegativePriceException.class, builder::build);
  }

  @Test
  void shouldThrowInvalidPriorityExceptionWhenPriorityIsNegative() {
    PriceTestBuilder builder = new PriceTestBuilder().withPriority(-1);

    assertThrows(InvalidPriorityException.class, builder::build);
  }

  @Test
  void shouldCreatePriceWhenValidData() {
    PriceTestBuilder builder = new PriceTestBuilder();

    assertDoesNotThrow(builder::build);
  }
}
