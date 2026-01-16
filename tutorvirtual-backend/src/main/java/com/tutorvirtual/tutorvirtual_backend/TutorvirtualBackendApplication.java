package com.tutorvirtual.tutorvirtual_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TutorvirtualBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TutorvirtualBackendApplication.class, args);
	}

}
