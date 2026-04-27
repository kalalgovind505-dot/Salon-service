package com.zosh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
public class SalonServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalonServiceApplication.class, args);
	}

}
