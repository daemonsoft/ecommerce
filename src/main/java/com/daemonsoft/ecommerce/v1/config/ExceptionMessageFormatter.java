package com.daemonsoft.ecommerce.v1.config;

import java.util.Objects;
import org.springframework.beans.TypeMismatchException;
import org.springframework.web.server.ServerWebInputException;

public class ExceptionMessageFormatter {

  private ExceptionMessageFormatter() {
    // Prevent instantiation
  }

  public static String formatValidationError(Exception ex) {
    if (ex instanceof ServerWebInputException swie) {
      return formatServerWebInputException(swie);
    }
    return "Invalid request: " + ex.getCause();
  }

  private static String formatServerWebInputException(ServerWebInputException swie) {
    String messageCause = swie.getReason();
    String parameterName = "unknown";
    String value = "unknown";

    var cause = swie.getCause();
    if (cause instanceof TypeMismatchException tme) {
      if (Objects.nonNull(tme.getPropertyName())) {
        parameterName = tme.getPropertyName();
      }
      if (Objects.nonNull(tme.getValue())) {
        value = tme.getValue().toString();
      }
    }

    return String.format(
        "Invalid request: Invalid value '%s' for parameter '%s'. %s",
        value, parameterName, messageCause);
  }
}
