package com.barclays.card.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.barclays.card.bot")
public class BotRuleProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(BotRuleProcessorApplication.class, args);
	}

}
