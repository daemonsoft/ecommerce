package com.daemonsoft.ecommerce.v1.prices.domain.exception;

public class InvalidPriceRangeException extends RuntimeException {
  public InvalidPriceRangeException(String message) {
    super(message);
  }
}
