/**
 * 
 */
package com.barclays.card.bot.rules;

import java.util.Map;
import java.util.Optional;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.barclays.card.bot.bo.RuleResponse;
import com.barclays.card.bot.util.FileUtil;

/**
 * Checks file present in specified directories or not. If any of the file not
 * present will give details accordinglly.
 * 
 *
 */
@Service
@Rule(name = "fileExistenceRule")
//@PropertySource("classpath:fileRules.properties")
//@ConfigurationProperties(ignoreUnknownFields = false,prefix = "file")
public class FileExistenceRule {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileExistenceRule.class);
	private static final String RULE_NAME = "FileExistenceRule";
	@Value("${file.source.directory}")
	private String sourceDir;

	@Value("${file.refrence.directory}")
	private String refrenceDir;

	private FileUtil fileUtil;

	public FileExistenceRule(FileUtil fileUtil) {
		super();
		this.fileUtil = fileUtil;
	}

	RuleResponse response = null;

	@Condition
	public boolean when() {
		LOGGER.info("File existance rule initaed.");
		response = new RuleResponse();
		response.setRuleName(RULE_NAME);
		boolean isFileMissing = false;

		Optional<Map<String, String>> diffMapOptional = fileUtil.checkFilePresence(refrenceDir, sourceDir);
		if (diffMapOptional.isPresent()) {
			isFileMissing=true;
			diffMapOptional.get().entrySet().forEach(p -> {
				LOGGER.error("File : " + p.getValue() + " not found.");
				response.addDetailedResult(p.getValue(), false);
			});
		} 
		response.setSuccess(false);
		RuleResponseContext.setResponse(response);
		return !isFileMissing;
	}

	/**
	 * For this case No any action is Required.
	 */
	@Action
	public void then() {
		response.setSuccess(true);
	}

}
