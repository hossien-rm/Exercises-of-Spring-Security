package com.shayan.springsecsection11;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class springsecsection11BankApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(springsecsection11BankApplication.class, args);
	}

}

