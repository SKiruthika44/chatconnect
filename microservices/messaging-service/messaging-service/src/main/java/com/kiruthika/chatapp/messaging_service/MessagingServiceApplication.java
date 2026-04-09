package com.kiruthika.chatapp.messaging_service;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableRabbit
public class MessagingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessagingServiceApplication.class, args);
	}

}
