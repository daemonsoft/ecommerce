package com.daemonsoft.ecommerce.v1.prices.infrastructure.persistence;

import java.time.LocalDateTime;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PriceSpringDataRepository extends ReactiveCrudRepository<PriceEntity, Long> {

  Flux<PriceEntity>
      findByProductIdAndBrandIdAndValidFromLessThanEqualAndValidUntilGreaterThanEqualOrderByPriorityDesc(
          Long productId,
          Long brandId,
          LocalDateTime applicationDate1,
          LocalDateTime applicationDate2);
}
