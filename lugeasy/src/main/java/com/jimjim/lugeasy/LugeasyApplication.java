package com.jimjim.lugeasy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LugeasyApplication {

	public static void main(String[] args) {
		SpringApplication.run(LugeasyApplication.class, args);
	}

}
