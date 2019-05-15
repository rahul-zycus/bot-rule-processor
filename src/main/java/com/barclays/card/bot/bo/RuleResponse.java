package com.barclays.card.bot.bo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RuleResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6703806397537536487L;
	private Boolean success = false;
	private String ruleName;
	private final Map<String, Boolean> detailResult = new HashMap<String, Boolean>();

	public Boolean isSuccess() {
		return success;
	}

	public void setSuccess(Boolean isSuccess) {
		this.success = isSuccess;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public void addDetailedResult(String key, boolean value) {
		detailResult.put(key, value);
	}

	public Map<String, Boolean> getDetailResult() {
		return detailResult;
	}

}
