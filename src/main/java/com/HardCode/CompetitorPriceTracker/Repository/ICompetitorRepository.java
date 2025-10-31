package com.HardCode.CompetitorPriceTracker.Repository;


import com.HardCode.CompetitorPriceTracker.Model.Competitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ICompetitorRepository extends JpaRepository<Competitor, UUID> {


    Optional<Competitor> findByUrl(String url);
}
