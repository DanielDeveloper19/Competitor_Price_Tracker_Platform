package com.HardCode.CompetitorPriceTracker.Controller;


import com.HardCode.CompetitorPriceTracker.Model.PriceHistory;
import com.HardCode.CompetitorPriceTracker.Model.Product;
import com.HardCode.CompetitorPriceTracker.Service.PriceHistoryService;
import com.HardCode.CompetitorPriceTracker.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class ProductController { //reviewed

    @Autowired
    private ProductService productService;

    @PostMapping("/createProduct")
    public ResponseEntity<Product> create(@RequestBody Product product) {
        return new ResponseEntity<>(productService.save(product), null, 200);
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<List<Product>> getAll() {
        return new ResponseEntity<>(productService.getAllProducts(), null, 200);
    }

    /* Maybe add later
    GET /api/products
// Returns list of all tracked products with current price

    GET /api/products/{id}/history
// Returns price history for specific product (last 30 days)

    POST /api/products
// Add new product URL to track

    DELETE /api/products/{id}
// Stop tracking a product
*/

}
