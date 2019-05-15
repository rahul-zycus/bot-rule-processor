package com.barclays.card.bot.config;

import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConifg {
	
	@Bean
	public RulesEngine initalisedRuleEngine() {
		return new DefaultRulesEngine();
	}

}
