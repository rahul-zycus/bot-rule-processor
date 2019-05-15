package com.barclays.card.bot.service;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.springframework.stereotype.Service;

import com.barclays.card.bot.bo.RuleResponse;
import com.barclays.card.bot.rules.FileExistenceRule;
import com.barclays.card.bot.rules.RuleResponseContext;

@Service
public class FileService {

	private RulesEngine ruleEngine;
	private FileExistenceRule fileExistanceRule;

	public FileService(RulesEngine ruleEngine, FileExistenceRule fileExistanceRule) {
		super();
		this.ruleEngine = ruleEngine;
		this.fileExistanceRule = fileExistanceRule;
	}

	public RuleResponse fireFileRule() {
		Rules rules = new Rules(fileExistanceRule);
		ruleEngine.fire(rules, new Facts());
		return RuleResponseContext.getResponse();
	}

}
