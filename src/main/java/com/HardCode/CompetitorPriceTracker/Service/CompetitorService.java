package com.HardCode.CompetitorPriceTracker.Service;


import com.HardCode.CompetitorPriceTracker.Model.Competitor;
import com.HardCode.CompetitorPriceTracker.Model.Product;
import com.HardCode.CompetitorPriceTracker.Repository.ICompetitorRepository;
import com.HardCode.CompetitorPriceTracker.Service.Scraper.ScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompetitorService {

    @Autowired
    ICompetitorRepository competitorRepository;
    @Autowired
    ScraperService scraperService;


    public List<Competitor> getAllCompetitors() {

        return competitorRepository.findAll();
    }

    public Competitor save(Competitor competitor) {
        Competitor savedCompetitor = competitorRepository.save(competitor);

        // Run scraper asynchronously (so API response isn't blocked)
        scraperService.scrapeShopifyProducts(savedCompetitor);

        return savedCompetitor;
    }


}
