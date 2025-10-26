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
public class ScraperService {

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    public List<Product> scrapeShopifyProducts(Competitor competitor) {
        List<Product> products = new ArrayList<>();

        try {
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
                double price = variants.getJSONObject(0).getDouble("price");

                Product product = new Product();
                product.setName(title);
                product.setCurrentPrice(BigDecimal.valueOf(price));
                product.setCompetitor(competitor);

                // Save or update product in DB
                productRepository.save(product);

                // Store price history
                PriceHistory history = new PriceHistory();
                history.setProduct(product);
                history.setPrice(price);
                history.setDate(LocalDateTime.now());
                priceHistoryRepository.save(history);

                products.add(product);
            }

        } catch (Exception e) {
            System.out.println("Error scraping " + competitor.getUrl() + ": " + e.getMessage());
        }

        return products;
    }



    private BigDecimal parsePrice(String priceText) {
        String clean = priceText.replaceAll("[^\\d.]", ""); // removes currency symbols
        if (clean.isEmpty()) return BigDecimal.ZERO;
        return new BigDecimal(clean);
    }




}
