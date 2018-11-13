package com.example.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class HelloApplication {

	public static void main(String[] args) {
		// Get environment variable
		String mod = System.getenv("thu");
		System.out.print(mod);

		SpringApplication.run(HelloApplication.class, args);
	}
}
