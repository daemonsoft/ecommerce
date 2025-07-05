package com.daemonsoft.ecommerce.v1.prices.domain;

public enum Currency {
  EUR("€"),
  USD("$");

  private final String symbol;

  Currency(String symbol) {
    this.symbol = symbol;
  }

  public String getSymbol() {
    return symbol;
  }

  @Override
  public String toString() {
    return name() + " (" + symbol + ")";
  }
}
