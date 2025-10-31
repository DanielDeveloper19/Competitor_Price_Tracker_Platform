package com.HardCode.CompetitorPriceTracker.Repository;


import com.HardCode.CompetitorPriceTracker.Model.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, UUID> {

    List<PriceHistory> getPriceHistoryByProductId(UUID productId);
}
