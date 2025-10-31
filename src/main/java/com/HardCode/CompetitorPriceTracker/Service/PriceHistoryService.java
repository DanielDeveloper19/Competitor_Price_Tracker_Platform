package com.HardCode.CompetitorPriceTracker.Service;


import com.HardCode.CompetitorPriceTracker.Model.PriceHistory;
import com.HardCode.CompetitorPriceTracker.Repository.PriceHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PriceHistoryService {

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    public PriceHistory savePriceHistory(PriceHistory priceHistory){
        return priceHistoryRepository.save(priceHistory);
    }

    public List<PriceHistory> getAllPriceHistory(){
        return priceHistoryRepository.findAll();
    }

    public List<PriceHistory> getPriceHistoryById(UUID id) {
        return priceHistoryRepository.getPriceHistoryByProductId(id);
    }
}
