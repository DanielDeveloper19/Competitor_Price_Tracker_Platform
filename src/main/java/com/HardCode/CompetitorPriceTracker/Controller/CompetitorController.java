package com.HardCode.CompetitorPriceTracker.Controller;


import com.HardCode.CompetitorPriceTracker.Model.Competitor;
import com.HardCode.CompetitorPriceTracker.Service.CompetitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class CompetitorController {

    @Autowired
    private CompetitorService competitorService;

    @PostMapping
    public ResponseEntity<Competitor> create(@RequestBody Competitor competitor) {
        return new ResponseEntity<>(competitorService.save(competitor), null, 200);
    }

    @GetMapping
    public ResponseEntity<List<Competitor>> getAll() {
        return new ResponseEntity<>(competitorService.getAllCompetitors(), null, 200);
    }

}
