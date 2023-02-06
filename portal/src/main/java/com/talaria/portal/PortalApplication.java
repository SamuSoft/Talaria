package com.talaria.portal;

import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PortalApplication {
	private final static String QUEUE_NAME = "Talaria";
	@Bean
	Queue queue() {
		return new Queue(QUEUE_NAME, false);
	}
	public static void main(String[] args) {
		SpringApplication.run(PortalApplication.class, args);
	}

}
