package com.daemonsoft.ecommerce.v1.config;

import com.daemonsoft.ecommerce.v1.prices.domain.exception.InvalidPriceRangeException;
import com.daemonsoft.ecommerce.v1.prices.domain.exception.InvalidPriorityException;
import com.daemonsoft.ecommerce.v1.prices.domain.exception.NegativePriceException;
import com.daemonsoft.ecommerce.v1.prices.domain.exception.PriceNotFoundException;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
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
  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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
    String message;
    String parameterName = "unknown";
    String value = "unknown";

    if (ex instanceof ServerWebInputException swie) {
      var messageCause = swie.getReason();
      var cause = swie.getCause();

      if (cause instanceof TypeMismatchException tme) {
        if (Objects.nonNull(tme.getPropertyName())) {
          parameterName = tme.getPropertyName();
        }
        if (Objects.nonNull(tme.getValue())) {
          value = tme.getValue().toString();
        }
      }

      message =
          String.format(
              "Invalid request: Invalid value '%s' for parameter '%s'. %s",
              value, parameterName, messageCause);
    } else {
      message = "Invalid request: " + ex.getCause();
    }

    return buildResponse(HttpStatus.BAD_REQUEST, message);
  }

  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<ErrorResponse>> handleAllOtherExceptions(Exception ex) {
    return buildResponse(
        HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + ex.getCause().getMessage());
  }

  private Mono<ResponseEntity<ErrorResponse>> buildResponse(HttpStatus status, String message) {
    String cleanMessage = message.replace("\"", "");
    return Mono.deferContextual(
        ctx -> {
          String traceId =
              ctx.getOrDefault(RequestTraceIdFilter.TRACE_ID_KEY, UUID.randomUUID().toString());
          logger.error(
              "Handling exception - status={} traceId={} message={}",
              status.value(),
              traceId,
              cleanMessage);
          ErrorResponse errorResponse = ErrorResponse.of(status.value(), cleanMessage, traceId);
          return Mono.just(ResponseEntity.status(status).body(errorResponse));
        });
  }
}
