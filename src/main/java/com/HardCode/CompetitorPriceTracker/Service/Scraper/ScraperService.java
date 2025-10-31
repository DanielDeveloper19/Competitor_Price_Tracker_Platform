package com.HardCode.CompetitorPriceTracker.Service.Scraper;

import com.HardCode.CompetitorPriceTracker.Model.Competitor;
import com.HardCode.CompetitorPriceTracker.Model.PriceHistory;
import com.HardCode.CompetitorPriceTracker.Model.Product;
import com.HardCode.CompetitorPriceTracker.Repository.IProductRepository;
import com.HardCode.CompetitorPriceTracker.Repository.PriceHistoryRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class ScraperService {

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;


    @Async
    public void scrapeShopifyProducts(Competitor competitor) {

        if (!"shopify".equalsIgnoreCase(competitor.getPlatform())) {
            System.out.println("⏩ Skipping non-Shopify competitor: " + competitor.getName());
            return;
        }

        List<Product> products = new ArrayList<>();

        try {
            System.out.println("✅ Starting to scrape competitor: " + competitor.getName());

            // 1️⃣ Build Shopify endpoint
            String url = competitor.getUrl();
            if (!url.endsWith("/products.json")) {
                url = url + "/products.json";
            }

            // 2️⃣ Fetch the JSON response
            Document doc = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .timeout(10_000)
                    .get();

            // 3️⃣ Parse JSON (Shopify returns plain JSON, so we use Jsoup body)
            String json = doc.body().text();

            // 4️⃣ Convert JSON → Java objects
            JSONObject obj = new JSONObject(json);
            JSONArray productsArray = obj.getJSONArray("products");

            for (int i = 0; i < productsArray.length(); i++) {
                JSONObject productJson = productsArray.getJSONObject(i);
                String title = productJson.getString("title");

                JSONArray variants = productJson.getJSONArray("variants");
                double newPrice = variants.getJSONObject(0).getDouble("price");

                // Try to find if the product already exists for this competitor
                Optional<Product> existingProductOpt = productRepository.findByNameAndCompetitor(title, competitor);

                Product product;

                if (existingProductOpt.isPresent()) {
                    product = existingProductOpt.get();
                    double oldPrice = product.getCurrentPrice();

                    // 🧠 Check if price has changed
                    if (Double.compare(oldPrice, newPrice) != 0) {
                        System.out.printf("💰 Price change detected for %s: %.2f → %.2f%n", title, oldPrice, newPrice);

                        product.setCurrentPrice(newPrice);
                        productRepository.save(product);

                        // Store price history (only when changed)
                        PriceHistory history = new PriceHistory();
                        history.setProduct(product);
                        history.setPrice(newPrice);
                        history.setDate(LocalDateTime.now());
                        priceHistoryRepository.save(history);
                    } else {
                        System.out.println("No price change for: " + title);
                    }

                } else {
                    // 🆕 New product → save and log
                    product = new Product();
                    product.setName(title);
                    product.setCurrentPrice(newPrice);
                    product.setCompetitor(competitor);

                    productRepository.save(product);

                    PriceHistory history = new PriceHistory();
                    history.setProduct(product);
                    history.setPrice(newPrice);
                    history.setDate(LocalDateTime.now());
                    priceHistoryRepository.save(history);

                    System.out.println("🆕 New product added: " + title);
                }

                products.add(product); //You could return this list if you want to take a look or use the values that were scraped
            }

            System.out.println("✅ Finished scraping competitor: " + competitor.getName());

        } catch (Exception e) {
            System.out.println("❌ Error scraping " + competitor.getUrl() + ": " + e.getMessage());
        }
    }



    private BigDecimal parsePrice(String priceText) {
        String clean = priceText.replaceAll("[^\\d.]", ""); // removes currency symbols
        if (clean.isEmpty()) return BigDecimal.ZERO;
        return new BigDecimal(clean);
    }




}
