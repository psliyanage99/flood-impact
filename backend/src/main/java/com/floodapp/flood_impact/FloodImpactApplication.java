package com.floodapp.flood_impact;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FloodImpactApplication {
	public static void main(String[] args) {
		SpringApplication.run(FloodImpactApplication.class, args);
	}

}
