package com.daemonsoft.ecommerce.v1.prices.domain.exception;

public class PriceNotFoundException extends RuntimeException {
  public PriceNotFoundException(String message) {
    super(message);
  }
}
