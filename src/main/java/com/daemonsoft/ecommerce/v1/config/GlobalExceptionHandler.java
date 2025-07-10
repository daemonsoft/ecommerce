package com.daemonsoft.ecommerce.v1.config;

import com.daemonsoft.ecommerce.v1.prices.domain.exception.InvalidPriceRangeException;
import com.daemonsoft.ecommerce.v1.prices.domain.exception.InvalidPriorityException;
import com.daemonsoft.ecommerce.v1.prices.domain.exception.NegativePriceException;
import com.daemonsoft.ecommerce.v1.prices.domain.exception.PriceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

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
        HttpStatus.INTERNAL_SERVER_ERROR, "Internal data error: " + ex.getCause().getMessage());
  }

  @ExceptionHandler({
    MethodArgumentTypeMismatchException.class,
    WebExchangeBindException.class,
    ServerWebInputException.class
  })
  public Mono<ResponseEntity<ErrorResponse>> handleBadRequest(Exception ex) {
    String message = ExceptionMessageFormatter.formatValidationError(ex);
    return buildResponse(HttpStatus.BAD_REQUEST, message);
  }

  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<ErrorResponse>> handleAllOtherExceptions(Exception ex) {
    return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + ex.getMessage());
  }

  private Mono<ResponseEntity<ErrorResponse>> buildResponse(HttpStatus status, String message) {
    return Mono.deferContextual(
        ctx -> {
          String traceId = ctx.getOrDefault(RequestTraceIdFilter.TRACE_ID_KEY, null);
          return ExceptionResponseBuilder.buildResponse(status, message, traceId);
        });
  }
}
