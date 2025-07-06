package com.daemonsoft.ecommerce.v1.prices.domain.exception;

public class NegativePriceException extends RuntimeException {
  public NegativePriceException(String message) {
    super(message);
  }
}
