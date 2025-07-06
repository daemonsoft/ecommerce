package com.daemonsoft.ecommerce.v1.prices.infrastructure;

import com.daemonsoft.ecommerce.v1.prices.application.GetProductPriceByBrandAndDate;
import com.daemonsoft.ecommerce.v1.prices.application.PriceResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/prices")
public class PriceController {

  private final GetProductPriceByBrandAndDate getProductPriceByBrandAndDate;

  public PriceController(GetProductPriceByBrandAndDate getProductPriceByBrandAndDate) {
    this.getProductPriceByBrandAndDate = getProductPriceByBrandAndDate;
  }

  @Operation(
      summary = "Get product price by brand and date",
      description =
          "Returns the applicable price for a given product, brand, and application date.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Price found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PriceResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "No applicable price found"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters")
      })
  @GetMapping
  public Mono<ResponseEntity<PriceResponseDTO>> getPrices(
      @Parameter(description = "Brand ID", example = "1", required = true) @RequestParam
          Long brandId,
      @Parameter(description = "Product ID", example = "35455", required = true) @RequestParam
          Long productId,
      @Parameter(
              description = "Application date in format yyyy-MM-dd-HH:mm:ss",
              example = "2020-06-14-10:00:00",
              required = true,
              schema = @Schema(type = "string"))
          @RequestParam
          @DateTimeFormat(pattern = "yyyy-MM-dd-HH:mm:ss")
          LocalDateTime applicationDate) {

    return this.getProductPriceByBrandAndDate
        .execute(productId, brandId, applicationDate)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }
}
