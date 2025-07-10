package com.daemonsoft.ecommerce.v1.prices.infrastructure.persistence;

import com.daemonsoft.ecommerce.v1.prices.domain.Currency;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Database table mapping for the prices table. This class represents the database schema and is
 * used for persistence operations.
 */
@Table("prices")
public class PriceTable {

  @Id private Long id;

  private Long brandId;

  private LocalDateTime validFrom;

  private LocalDateTime validUntil;

  private Long rateId;

  private Long productId;

  private Integer priority;

  private BigDecimal finalPrice;

  private String currencyCode;

  public PriceTable() {}

  public PriceTable(
      Long id,
      Long brandId,
      LocalDateTime validFrom,
      LocalDateTime validUntil,
      Long rateId,
      Long productId,
      Integer priority,
      BigDecimal finalPrice,
      Currency currency) {
    this.id = id;
    this.brandId = brandId;
    this.validFrom = validFrom;
    this.validUntil = validUntil;
    this.rateId = rateId;
    this.productId = productId;
    this.priority = priority;
    this.finalPrice = finalPrice;
    this.currencyCode = currency.name();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getBrandId() {
    return brandId;
  }

  public void setBrandId(Long brandId) {
    this.brandId = brandId;
  }

  public LocalDateTime getValidFrom() {
    return validFrom;
  }

  public void setValidFrom(LocalDateTime validFrom) {
    this.validFrom = validFrom;
  }

  public LocalDateTime getValidUntil() {
    return validUntil;
  }

  public void setValidUntil(LocalDateTime validUntil) {
    this.validUntil = validUntil;
  }

  public Long getRateId() {
    return rateId;
  }

  public void setRateId(Long rateId) {
    this.rateId = rateId;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public BigDecimal getFinalPrice() {
    return finalPrice;
  }

  public void setFinalPrice(BigDecimal finalPrice) {
    this.finalPrice = finalPrice;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }
}
