package com.detectify.codingchallenge.webdriverservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class WebDriverServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebDriverServiceApplication.class, args);
	}

}
