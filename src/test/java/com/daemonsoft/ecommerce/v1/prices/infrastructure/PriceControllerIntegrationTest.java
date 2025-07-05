package com.daemonsoft.ecommerce.v1.prices.infrastructure;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class PriceControllerIntegrationTest {

  @Autowired private WebTestClient webTestClient;

  private static final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");

  @Test
  void testPriceNotFound() {
    this.webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/v1/prices")
                    .queryParam("brandId", "1")
                    .queryParam("productId", "44444")
                    .queryParam(
                        "applicationDate", LocalDateTime.of(2025, 6, 14, 10, 0).format(formatter))
                    .build())
        .exchange()
        .expectStatus()
        .isNotFound();
  }

  @Test
  void testPriceBadRequest() {
    this.webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/v1/prices")
                    .queryParam("brandId", "1")
                    .queryParam("productId", "35455")
                    .queryParam("applicationDate", "invalid-date")
                    .build())
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  @Test
  void testPriceAt2020_06_14_10_00() {
    this.webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/v1/prices")
                    .queryParam("brandId", "1")
                    .queryParam("productId", "35455")
                    .queryParam(
                        "applicationDate", LocalDateTime.of(2020, 6, 14, 10, 0).format(formatter))
                    .build())
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.rate")
        .isEqualTo(1);
  }

  @Test
  void testPriceAt2020_06_14_16_00() {
    this.webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/v1/prices")
                    .queryParam("brandId", "1")
                    .queryParam("productId", "35455")
                    .queryParam(
                        "applicationDate", LocalDateTime.of(2020, 6, 14, 16, 0).format(formatter))
                    .build())
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.rate")
        .isEqualTo(2);
  }

  @Test
  void testPriceAt2020_06_14_21_00() {
    this.webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/v1/prices")
                    .queryParam("brandId", "1")
                    .queryParam("productId", "35455")
                    .queryParam(
                        "applicationDate", LocalDateTime.of(2020, 6, 14, 21, 0).format(formatter))
                    .build())
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.rate")
        .isEqualTo(1);
  }

  @Test
  void testPriceAt2020_06_15_10_00() {
    this.webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/v1/prices")
                    .queryParam("brandId", "1")
                    .queryParam("productId", "35455")
                    .queryParam(
                        "applicationDate", LocalDateTime.of(2020, 6, 15, 10, 0).format(formatter))
                    .build())
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.rate")
        .isEqualTo(3);
  }

  @Test
  void testPriceAt2020_06_16_21_00() {
    this.webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/v1/prices")
                    .queryParam("brandId", "1")
                    .queryParam("productId", "35455")
                    .queryParam(
                        "applicationDate", LocalDateTime.of(2020, 6, 16, 21, 0).format(formatter))
                    .build())
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.rate")
        .isEqualTo(4);
  }
}
