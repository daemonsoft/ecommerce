package com.daemonsoft.ecommerce.v1.prices.application;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Price response data including product, brand, dates and final price")
public record PriceResponseDTO(
    @Schema(description = "Product ID", example = "35455") Long productId,
    @Schema(description = "Brand ID", example = "1") Long brandId,
    @Schema(description = "Tariff or rate ID", example = "2") Long rate,
    @Schema(description = "Price validity start datetime", example = "2020-06-14-10:00:00")
        LocalDateTime validFrom,
    @Schema(description = "Price validity end datetime", example = "2020-06-14-18:30:00")
        LocalDateTime validUntil,
    @Schema(description = "Final price to pay", example = "35.50") BigDecimal finalPrice) {}
