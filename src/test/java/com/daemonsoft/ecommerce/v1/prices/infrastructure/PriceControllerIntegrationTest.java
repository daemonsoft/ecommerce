package com.daemonsoft.ecommerce.v1.prices.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class PriceControllerIntegrationTest {

  @Autowired private WebTestClient webTestClient;

  private static final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");

  @Test
  void shouldReturnPriceWhenValidRequest() {
    // Given: Valid request parameters
    Long brandId = 1L;
    Long productId = 35455L;
    LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);

    // When & Then: Request should return successful response
    this.webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/v1/prices")
                    .queryParam("brandId", brandId)
                    .queryParam("productId", productId)
                    .queryParam("applicationDate", applicationDate.format(formatter))
                    .build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.brandId")
        .isEqualTo(brandId)
        .jsonPath("$.productId")
        .isEqualTo(productId)
        .jsonPath("$.rateId")
        .isEqualTo(1)
        .jsonPath("$.finalPrice")
        .isEqualTo(35.50);
  }

  @Test
  void shouldReturnPriceNotFoundWhenNoMatchingPrice() {
    // Given: Request with non-existent product
    Long brandId = 1L;
    Long productId = 99999L; // Non-existent product
    LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);

    // When & Then: Request should return 404
    this.webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/v1/prices")
                    .queryParam("brandId", brandId)
                    .queryParam("productId", productId)
                    .queryParam("applicationDate", applicationDate.format(formatter))
                    .build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectBody()
        .jsonPath("$.status")
        .isEqualTo(404)
        .jsonPath("$.message")
        .value(message -> assertThat(message).asString().contains("Price not found"));
  }

  @Test
  void shouldReturnBadRequestWhenInvalidDateFormat() {
    // Given: Request with invalid date format
    Long brandId = 1L;
    Long productId = 35455L;
    String invalidDate = "invalid-date";

    // When & Then: Request should return 400
    this.webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/v1/prices")
                    .queryParam("brandId", brandId)
                    .queryParam("productId", productId)
                    .queryParam("applicationDate", invalidDate)
                    .build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath("$.status")
        .isEqualTo(400)
        .jsonPath("$.message")
        .value(message -> assertThat(message).asString().contains("Invalid value"));
  }

  @Test
  void shouldReturnBadRequestWhenMissingRequiredParameters() {
    // Given: Request missing required parameters
    Long brandId = 1L;
    // Missing productId and applicationDate

    // When & Then: Request should return 400
    this.webTestClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/v1/prices").queryParam("brandId", brandId).build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  @Test
  void shouldReturnBadRequestWhenInvalidParameterTypes() {
    // Given: Request with invalid parameter types
    String invalidBrandId = "not-a-number";
    Long productId = 35455L;
    LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);

    // When & Then: Request should return 400
    this.webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/v1/prices")
                    .queryParam("brandId", invalidBrandId)
                    .queryParam("productId", productId)
                    .queryParam("applicationDate", applicationDate.format(formatter))
                    .build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath("$.status")
        .isEqualTo(400)
        .jsonPath("$.message")
        .value(message -> assertThat(message).asString().contains("Invalid value"));
  }

  @Test
  void shouldReturnCorrectPriceForDifferentDateRanges() {
    // Given: Request for different date ranges
    Long brandId = 1L;
    Long productId = 35455L;

    // Test case 1: Date within first price range (rate 1, priority 0)
    LocalDateTime date1 = LocalDateTime.of(2020, 6, 14, 10, 0);

    // Test case 2: Date within second price range (rate 2, priority 1)
    LocalDateTime date2 = LocalDateTime.of(2020, 6, 14, 16, 0);

    // When & Then: First date should return first price
    this.webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/v1/prices")
                    .queryParam("brandId", brandId)
                    .queryParam("productId", productId)
                    .queryParam("applicationDate", date1.format(formatter))
                    .build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.rateId")
        .isEqualTo(1)
        .jsonPath("$.finalPrice")
        .isEqualTo(35.50);

    // When & Then: Second date should return second price
    this.webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/v1/prices")
                    .queryParam("brandId", brandId)
                    .queryParam("productId", productId)
                    .queryParam("applicationDate", date2.format(formatter))
                    .build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.rateId")
        .isEqualTo(2)
        .jsonPath("$.finalPrice")
        .isEqualTo(25.45);
  }

  @Test
  void shouldIncludeTraceIdInErrorResponse() {
    // Given: Request that will cause an error
    Long brandId = 1L;
    Long productId = 35455L;
    String invalidDate = "invalid-date";

    // When & Then: Error response should include traceId
    this.webTestClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/v1/prices")
                    .queryParam("brandId", brandId)
                    .queryParam("productId", productId)
                    .queryParam("applicationDate", invalidDate)
                    .build())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath("$.traceId")
        .exists()
        .jsonPath("$.timestamp")
        .exists();
  }
}
