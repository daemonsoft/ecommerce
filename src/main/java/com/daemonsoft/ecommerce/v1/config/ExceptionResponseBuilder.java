package com.daemonsoft.ecommerce.v1.config;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public class ExceptionResponseBuilder {

  private static final Logger logger = LoggerFactory.getLogger(ExceptionResponseBuilder.class);

  private ExceptionResponseBuilder() {
    // Prevent instantiation
  }

  /**
   * Builds an error response with proper logging and trace ID management.
   *
   * @param status the HTTP status code
   * @param message the error message
   * @param traceId the request trace ID
   * @return a Mono containing the ResponseEntity
   */
  public static Mono<ResponseEntity<ErrorResponse>> buildResponse(
      HttpStatus status, String message, String traceId) {

    String cleanMessage = message.replace("\"", "");
    String finalTraceId = traceId != null ? traceId : UUID.randomUUID().toString();

    logger.error(
        "Handling exception - status={} traceId={} message={}",
        status.value(),
        finalTraceId,
        cleanMessage);

    ErrorResponse errorResponse = ErrorResponse.of(status.value(), cleanMessage, finalTraceId);
    return Mono.just(ResponseEntity.status(status).body(errorResponse));
  }
}
