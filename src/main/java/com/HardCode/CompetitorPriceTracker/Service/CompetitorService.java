package com.HardCode.CompetitorPriceTracker.Service;


import com.HardCode.CompetitorPriceTracker.Model.Competitor;
import com.HardCode.CompetitorPriceTracker.Model.Product;
import com.HardCode.CompetitorPriceTracker.Repository.ICompetitorRepository;
import com.HardCode.CompetitorPriceTracker.Service.Scraper.ScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

        Optional<Competitor> existing = competitorRepository.findByUrl(competitor.getUrl());

        if (existing.isPresent()) {
            System.out.println("⚠️ Competitor already exists: " + competitor.getUrl());
            return existing.get();
        }

        Competitor savedCompetitor = competitorRepository.save(competitor);

        // Run scraper asynchronously (so API response isn't blocked)
        scraperService.scrapeShopifyProducts(savedCompetitor);

        return savedCompetitor;
    }


}
