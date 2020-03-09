package com.arsalaan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ShushiShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShushiShopApplication.class, args);

	}
}
