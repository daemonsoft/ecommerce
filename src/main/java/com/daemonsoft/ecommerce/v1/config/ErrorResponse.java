package com.daemonsoft.ecommerce.v1.config;

import java.time.Instant;

public record ErrorResponse(String timestamp, int status, String message, String traceId) {
  public static ErrorResponse of(int status, String message, String traceId) {
    return new ErrorResponse(Instant.now().toString(), status, message, traceId);
  }
}
