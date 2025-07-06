package com.daemonsoft.ecommerce.v1.config;

import com.daemonsoft.ecommerce.v1.prices.domain.exception.InvalidPriceRangeException;
import com.daemonsoft.ecommerce.v1.prices.domain.exception.InvalidPriorityException;
import com.daemonsoft.ecommerce.v1.prices.domain.exception.NegativePriceException;
import com.daemonsoft.ecommerce.v1.prices.domain.exception.PriceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAllExceptions(
      Exception ex, ServerHttpRequest request) {
    String traceId = request.getHeaders().getFirst("X-Request-Id");
    if (traceId == null) {
      traceId = "no-trace-id";
    }

    logger.error("Exception caught with traceId {}: {}", traceId, ex.getMessage(), ex);

    ErrorResponse error =
        ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), traceId);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  @ExceptionHandler(PriceNotFoundException.class)
  public Mono<ResponseEntity<ErrorResponse>> handleNotFound(PriceNotFoundException ex) {
    return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler({
    InvalidPriceRangeException.class,
    NegativePriceException.class,
    InvalidPriorityException.class
  })
  public Mono<ResponseEntity<ErrorResponse>> handleInternalErrors(RuntimeException ex) {
    return buildResponse(
        HttpStatus.INTERNAL_SERVER_ERROR, "Internal data error: " + ex.getMessage());
  }

  @ExceptionHandler({
    MethodArgumentTypeMismatchException.class,
    WebExchangeBindException.class,
    ServerWebInputException.class
  })
  public Mono<ResponseEntity<ErrorResponse>> handleBadRequest(Exception ex) {
    return buildResponse(HttpStatus.BAD_REQUEST, "Invalid request: " + ex.getMessage());
  }

  private Mono<ResponseEntity<ErrorResponse>> buildResponse(HttpStatus status, String message) {
    String cleanMessage = message.replace("\"", "");
    return Mono.deferContextual(
        ctx -> {
          String traceId =
              ctx.getOrDefault(
                  RequestTraceIdFilter.TRACE_ID_KEY, java.util.UUID.randomUUID().toString());
          ErrorResponse errorResponse = ErrorResponse.of(status.value(), cleanMessage, traceId);
          return Mono.just(ResponseEntity.status(status).body(errorResponse));
        });
  }
}
