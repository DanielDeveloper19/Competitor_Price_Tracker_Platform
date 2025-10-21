package com.HardCode.CompetitorPriceTracker.Service;


import com.HardCode.CompetitorPriceTracker.Model.Competitor;
import com.HardCode.CompetitorPriceTracker.Model.Product;
import com.HardCode.CompetitorPriceTracker.Repository.ICompetitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompetitorService {

    @Autowired
    ICompetitorRepository competitorRepository;


    public List<Competitor> getAllCompetitors() {

        return competitorRepository.findAll();
    }

    public Competitor save(Competitor competitor){

         return competitorRepository.save(competitor);
    }


}
