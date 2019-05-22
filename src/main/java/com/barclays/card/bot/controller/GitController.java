/**
 * 
 */
package com.barclays.card.bot.controller;

import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.barclays.card.bot.service.GitService;

/**
 * @author Rahul
 *
 */
@RestController
@RequestMapping("/git")
public class GitController {
	private static final Logger LOGGER = LoggerFactory.getLogger(GitController.class);

	private GitService gitService;

	public GitController(GitService gitService) {
		super();
		this.gitService = gitService;
	}

	@GetMapping("/exists/{repo}")
	public ResponseEntity<String> checkGitRepo(@PathVariable("repo") String repoName) {
		if (repoName.startsWith("http")) {
			return ResponseEntity.badRequest().body("URI_MUST_BE_SSH_TYPE");
		} else if (!repoName.endsWith(".git")) {
			return ResponseEntity.badRequest().body("INVALID_URI");
		}
		return ResponseEntity.ok().body(gitService.isRepoExists(repoName) == true ? "Repo Present" : "Not Valid Repo");
	}

	@PostMapping("/clone/{repo}/{branchName}")
	public ResponseEntity<Void> cloneRepo(@PathVariable("repo") String repoName,
			@PathVariable("branchName") String branchName) throws GitAPIException {
		try {
			gitService.clone(repoName, branchName);
		} catch (GitAPIException e) {
			LOGGER.error("ERROR_WHILE_CLONE_REPO", e);
			throw e;
		}
		return (ResponseEntity<Void>) ResponseEntity.ok();
	}

	@GetMapping("/branches")
	public ResponseEntity<List<String>> getBranch(@RequestParam(name = "repo") String repoName) {
		return ResponseEntity.ok().body(gitService.getAllBranch(repoName));
	}

}
