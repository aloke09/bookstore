package com.bridgelabz.cartms2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class Cartms2Application {

	public static void main(String[] args) {
		SpringApplication.run(Cartms2Application.class, args);
	}

}
