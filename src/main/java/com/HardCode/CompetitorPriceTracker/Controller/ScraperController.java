package com.HardCode.CompetitorPriceTracker.Controller;


import com.HardCode.CompetitorPriceTracker.Model.Competitor;
import com.HardCode.CompetitorPriceTracker.Model.Product;
import com.HardCode.CompetitorPriceTracker.Repository.ICompetitorRepository;
import com.HardCode.CompetitorPriceTracker.Service.Scraper.ScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ScraperController {

    @Autowired
    private ScraperService scraperService;
    @Autowired
    private ICompetitorRepository competitorRepository;

    @PostMapping("/run")
    public List<Product> scrapeAllCompetitors() {
        List<Competitor> competitors = competitorRepository.findAll();
        List<Product> allProducts = new ArrayList<>();

        for (Competitor c : competitors) {
            allProducts.addAll(scraperService.scrapeShopifyProducts(c));
        }

        return allProducts;
    }

}
