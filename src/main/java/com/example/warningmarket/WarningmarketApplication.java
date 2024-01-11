package com.example.warningmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WarningmarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(WarningmarketApplication.class, args);
	}

}
