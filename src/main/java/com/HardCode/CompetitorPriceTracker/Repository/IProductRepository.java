package com.HardCode.CompetitorPriceTracker.Repository;


import com.HardCode.CompetitorPriceTracker.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IProductRepository extends JpaRepository<Product, UUID> {
}
