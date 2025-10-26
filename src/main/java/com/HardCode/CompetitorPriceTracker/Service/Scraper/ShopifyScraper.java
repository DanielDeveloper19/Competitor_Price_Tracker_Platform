package com.HardCode.CompetitorPriceTracker.Service.Scraper;

import com.HardCode.CompetitorPriceTracker.Model.Competitor;
import com.HardCode.CompetitorPriceTracker.Model.PriceHistory;
import com.HardCode.CompetitorPriceTracker.Model.Product;
import com.HardCode.CompetitorPriceTracker.Repository.IProductRepository;
import com.HardCode.CompetitorPriceTracker.Repository.PriceHistoryRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShopifyScraper {

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    public List<Product> scrapeShopifyProducts(Competitor competitor) {
        List<Product> scrapedProducts = new ArrayList<>();

        try {
            // Shopify sites often expose all products here:
            String url = competitor.getUrl() + "/collections/all";
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();

            // CSS selectors depend on theme, but ".product-card" or ".grid-product" is common
            Elements products = doc.select(".product-card, .grid-product");

            for (Element productEl : products) {
                String name = productEl.select(".product-card__title, .grid-product__title").text();
                String priceText = productEl.select(".product-card__price, .grid-product__price").text();

                // Clean and parse price (remove $ or currency symbol)
                Double price = parsePrice(priceText);

                // Create and save product
                Product product = new Product();
                product.setName(name);
                product.setCurrentPrice(price);
                product.setCompetitor(competitor);

                productRepository.save(product);

                // Add to price history
                PriceHistory history = new PriceHistory();
                history.setProduct(product);
                history.setPrice(price);
                history.setDate(LocalDateTime.now());
                priceHistoryRepository.save(history);

                scrapedProducts.add(product);
            }
        } catch (IOException e) {
            System.err.println("Error scraping " + competitor.getUrl() + ": " + e.getMessage());
        }

        return scrapedProducts;
    }

    private Double parsePrice(String priceText) {
        if (priceText == null || priceText.isBlank()) {
            return 0.0;
        }

        try {
            // Remove everything except numbers, commas, and dots
            String cleaned = priceText.replaceAll("[^\\d.,]", "");

            // Handle formats like "1,299.99" or "1.299,99"
            if (cleaned.contains(",") && cleaned.contains(".")) {
                if (cleaned.lastIndexOf(",") > cleaned.lastIndexOf(".")) {
                    cleaned = cleaned.replace(".", "").replace(",", ".");
                } else {
                    cleaned = cleaned.replace(",", "");
                }
            } else if (cleaned.contains(",")) {
                // If there's only a comma, assume it's a decimal separator
                cleaned = cleaned.replace(",", ".");
            }

            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            System.err.println("⚠️ Could not parse price: " + priceText);
            return 0.0;
        }
    }


}
