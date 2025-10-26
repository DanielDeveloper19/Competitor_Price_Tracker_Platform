package com.HardCode.CompetitorPriceTracker.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Getter
@Setter
public class Competitor {//reviewed

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String url;
    private String platform;
    private String country;

    @OneToMany(mappedBy = "competitor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;


}
