package com.dnd.ahaive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AhaiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(AhaiveApplication.class, args);
	}

}
