package com.HardCode.CompetitorPriceTracker.Controller;


import com.HardCode.CompetitorPriceTracker.Model.Competitor;
import com.HardCode.CompetitorPriceTracker.Model.PriceHistory;
import com.HardCode.CompetitorPriceTracker.Service.CompetitorService;
import com.HardCode.CompetitorPriceTracker.Service.PriceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@CrossOrigin(origins = "*")
public class PriceHistoryController { //reviewed

    @Autowired
    private PriceHistoryService priceHistoryService;

    @PostMapping("/createPriceHistory")
    public ResponseEntity<PriceHistory> create(@RequestBody PriceHistory priceHistory) {
        return new ResponseEntity<>(priceHistoryService.savePriceHistory(priceHistory), null, 200);
    }

    @GetMapping("/getAllPriceHistories")
    public ResponseEntity<List<PriceHistory>> getAll() {
        return new ResponseEntity<>(priceHistoryService.getAllPriceHistory(), null, 200);
    }

    @GetMapping("/price-history/{productId}")
    public ResponseEntity<List<PriceHistory>> getPriceHistoryById(@PathVariable("productId") UUID productId) {
        return new ResponseEntity<>(priceHistoryService.getPriceHistoryById(productId), null, 200);
    }

}
