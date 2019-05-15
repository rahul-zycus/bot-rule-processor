/**
 * 
 */
package com.barclays.card.bot.rules;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.barclays.card.bot.bo.RuleResponse;

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
	private String fileName = "fileRules.properties";
	private static final String RULE_NAME = "FileExistenceRule";

	RuleResponse response = null;

	private Properties properties;

	@PostConstruct
	public void init() throws IOException {
		try (InputStream in = FileExistenceRule.class.getClassLoader().getResourceAsStream(fileName)) {
			if (in == null) {
				throw new IOException("ERROR_WHILE_READING_FILE");
			}
			properties = new Properties();
			properties.load(in);
		} catch (IOException e) {
			LOGGER.error("Error while reading file : " + fileName, e);
			throw e;
		}
	}

	@Condition
	public boolean when() {
		response = new RuleResponse();
		response.setRuleName(RULE_NAME);
		boolean isFileMissing = false;
		for (Entry entry : properties.entrySet()) {
			File file = new File(entry.getValue().toString() + "/" + entry.getKey().toString());
			if (!file.exists()) {
				LOGGER.error("Error File Missing." + file.getName());
				isFileMissing = true;
				response.addDetailedResult(entry.getKey().toString(), false);
			} else
				response.addDetailedResult(entry.getKey().toString(), true);
		}
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
