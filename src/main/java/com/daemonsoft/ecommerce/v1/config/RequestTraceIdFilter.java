package com.daemonsoft.ecommerce.v1.config;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class RequestTraceIdFilter implements WebFilter {

  private static final Logger logger = LoggerFactory.getLogger(RequestTraceIdFilter.class);
  public static final String TRACE_ID_KEY = "traceId";

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String traceId = exchange.getRequest().getHeaders().getFirst("X-Request-Id");

    if (traceId == null || traceId.isBlank()) {
      traceId = UUID.randomUUID().toString();
    }

    logger.debug("RequestTraceIdFilter - traceId={}", traceId);

    String finalTraceId = traceId;
    return chain.filter(exchange).contextWrite(ctx -> ctx.put(TRACE_ID_KEY, finalTraceId));
  }
}
