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

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        return new ResponseEntity<>(productService.save(product), null, 200);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return new ResponseEntity<>(productService.getAllProducts(), null, 200);
    }

}
