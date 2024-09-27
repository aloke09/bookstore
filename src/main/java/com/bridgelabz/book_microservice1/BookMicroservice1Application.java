package com.bridgelabz.book_microservice1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BookMicroservice1Application {

	public static void main(String[] args) {
		SpringApplication.run(BookMicroservice1Application.class, args);
	}

}
