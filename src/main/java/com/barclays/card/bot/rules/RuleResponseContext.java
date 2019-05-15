package com.barclays.card.bot.rules;

import com.barclays.card.bot.bo.RuleResponse;

public class RuleResponseContext {
	private static final ThreadLocal<RuleResponse> local = new ThreadLocal<RuleResponse>();
	
	public static void setResponse(RuleResponse response) {
		local.set(response);
	}
	public static RuleResponse getResponse() {
		return local.get();
	}
	

}
