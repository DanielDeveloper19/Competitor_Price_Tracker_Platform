package com.HardCode.CompetitorPriceTracker.Config.Scheduler;


import com.HardCode.CompetitorPriceTracker.Repository.ICompetitorRepository;
import com.HardCode.CompetitorPriceTracker.Service.Scraper.ScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScraperScheduler {

    @Autowired
    private ScraperService scraperService;
    @Autowired
    private ICompetitorRepository competitorRepository;

    @Scheduled(cron = "0 0 8 * * *") // every day at 8 AM
    public void runDailyScrape() {
        competitorRepository.findAll().forEach(c ->
                scraperService.scrapeShopifyProducts(c)
        );
    }

}
