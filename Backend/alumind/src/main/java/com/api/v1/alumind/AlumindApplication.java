package com.api.v1.alumind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AlumindApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlumindApplication.class, args);
	}

}
