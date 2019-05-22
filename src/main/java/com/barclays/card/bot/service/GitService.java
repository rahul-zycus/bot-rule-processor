/**
 * 
 */
package com.barclays.card.bot.service;

import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import com.barclays.card.bot.util.GitUtil;

/**
 * @author Rahul
 *
 */
@Service
public class GitService {
	
	private GitUtil gitUtil;

	public GitService(GitUtil gitUtil) {
		super();
		this.gitUtil = gitUtil;
	}
	
	public Boolean isRepoExists(String gitRepo) {
		return gitUtil.isValidRepo(gitRepo);
	}
	public void clone(String gitRepo,String brnach) throws GitAPIException {
		gitUtil.cloneRepo(gitRepo, brnach);
	}
	
	public List<String> getAllBranch(String gitRepo){
		return gitUtil.fetchGitBranches(gitRepo);
	}
}
