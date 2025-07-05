package com.daemonsoft.ecommerce.v1.prices.infrastructure.persistence;

import com.daemonsoft.ecommerce.v1.prices.domain.Price;
import com.daemonsoft.ecommerce.v1.prices.domain.PriceRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class PriceRepositoryH2 implements PriceRepository {

  private final PriceSpringDataRepository springDataRepo;

  public PriceRepositoryH2(PriceSpringDataRepository springDataRepo) {
    this.springDataRepo = springDataRepo;
  }

  @Override
  public Mono<Price> findPrice(Long brandId, Long productId, LocalDateTime applicationDate) {
    return this.springDataRepo
        .findByProductIdAndBrandIdAndValidFromLessThanEqualAndValidUntilGreaterThanEqualOrderByPriorityDesc(
            productId, brandId, applicationDate, applicationDate)
        .next()
        .map(PriceEntityMapper::toDomain);
  }
}
