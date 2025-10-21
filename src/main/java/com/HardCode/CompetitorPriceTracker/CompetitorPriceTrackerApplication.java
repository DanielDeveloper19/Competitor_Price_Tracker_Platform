package com.HardCode.CompetitorPriceTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CompetitorPriceTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompetitorPriceTrackerApplication.class, args);
	}

}
