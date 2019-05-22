/**
 * 
 */
package com.barclays.card.bot.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.TransportCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig.Host;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.util.FS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.barclays.card.bot.exception.GitException;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * @author Rahul
 *
 */
@Component
public class GitUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(GitUtil.class);

	@Value("${git.ssh.file}")
	private String sshKeyFile;
	@Value("${git.ssh.passPhrase}")
	private String passPhrase;
	@Value("${git.temp.location}")
	private String tempFileLocation;

	public static void main(String[] args) throws GitAPIException {
		// isValidRepo("git@github.com:rahul-zycus/spring5webapp.git");
		GitUtil git = new GitUtil();
		git.cloneRepo("git@github.com:rahul-zycus/bot-rule-processor.git", "refs/heads/master");
	}

	public List<String> fetchGitBranches(String gitUrl) {
		Collection<Ref> refs;
		List<String> branches = new ArrayList<String>();
		try {
			LsRemoteCommand cmd = Git.lsRemoteRepository().setHeads(true).setRemote(gitUrl);
			// .call();
			cmd = setSShKey(cmd);
			refs = cmd.call();
			for (Ref ref : refs) {
				branches.add(ref.getName().substring(ref.getName().lastIndexOf("/") + 1, ref.getName().length()));
			}
			Collections.sort(branches);
		} catch (GitAPIException e) {
			LOGGER.error(" FETCH_BRANCH_ERROR", e);
		}
		return branches;
	}

	public boolean isValidRepo(String gitRepo) {
		LsRemoteCommand lsCmd = new LsRemoteCommand(null);
		lsCmd.setRemote(gitRepo);
		lsCmd = setSShKey(lsCmd);
		try {
			lsCmd.call().toString();
		} catch (TransportException e) {
			LOGGER.error("ERROR_NO_REPO_EXISTS", e);
			return false;
		} catch (GitAPIException e) {
			LOGGER.error("ERROR_WHILE_ACCESSING_GIT");
			return false;
		}
		return true;
	}

	public <T extends TransportCommand> T setSShKey(T gitCommand) {
		gitCommand.setTransportConfigCallback((transport) -> {
			SshTransport sshTransport = (SshTransport) transport;
			sshTransport.setSshSessionFactory(getDefaultJschFactory());
		});
		return gitCommand;
	}

	/*
	 * TODO To load file name and passphrase from the property file.
	 */
	private SshSessionFactory getDefaultJschFactory() {
		return new JschConfigSessionFactory() {
			@Override
			protected void configure(Host hc, Session session) {
			}

			@Override
			protected JSch createDefaultJSch(FS fs) throws JSchException {
				JSch defaultJSch = super.createDefaultJSch(fs);
				defaultJSch.addIdentity(sshKeyFile, passPhrase);
				return defaultJSch;
			}
		};
	}

	public void cloneRepo(String gitRepo, String gitBranch) throws GitAPIException {
		if (isValidRepo(gitRepo)) {
			try {
				File file = new File(tempFileLocation);
				file.deleteOnExit();
				CloneCommand cloneCommand = Git.cloneRepository().setURI(gitRepo)// .setCloneAllBranches(true)
						.setBranchesToClone(Arrays.asList(gitBranch)).setBranch(gitBranch).setCloneAllBranches(false)
						.setDirectory(new File(tempFileLocation));
				cloneCommand = setSShKey(cloneCommand);
				cloneCommand.call();
			} catch (Exception e) {
				throw new GitException("ERROR_WHILE_CLONING_REPO", e);
			}
		} else {
			throw new GitException("ERROR_NO_REPO_EXISTS");
		}

	}

}
