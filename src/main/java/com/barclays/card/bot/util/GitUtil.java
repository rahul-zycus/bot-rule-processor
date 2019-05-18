/**
 * 
 */
package com.barclays.card.bot.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.GitCommand;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.TransportCommand;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.errors.NoRemoteRepositoryException;
import org.eclipse.jgit.lib.GitmoduleEntry;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.util.FS;
import org.eclipse.jgit.transport.OpenSshConfig.Host;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.barclays.card.bot.exception.GitException;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * @author Rahul
 *
 */
public class GitUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(GitUtil.class);

	private static String sshKeyFile = "D:\\code\\key\\privkey";
	private static String passPhrase = "test";
	private static String tempFileLocation = "D:\\temp";

	public static void main(String[] args) throws GitAPIException {
		//isValidRepo("git@github.com:rahul-zycus/spring5webapp.git");
		cloneRepo("git@github.com:rahul-zycus/spring5webapp.git", "refs/heads/master");
	}

	public static boolean isValidRepo(String gitRepo) {
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

	public static <T extends TransportCommand> T setSShKey(T gitCommand) {
		gitCommand.setTransportConfigCallback((transport) -> {
			SshTransport sshTransport = (SshTransport) transport;
			sshTransport.setSshSessionFactory(getDefaultJschFactory());
		});
		return gitCommand;
	}

	/*
	 * TODO To load file name and passphrase from the property file.
	 */
	private static SshSessionFactory getDefaultJschFactory() {
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

	public static void cloneRepo(String gitRepo, String gitBranch) throws GitAPIException {
		if (isValidRepo(gitRepo)) {
			try {
				CloneCommand cloneCommand = Git.cloneRepository().setURI(gitRepo).setNoCheckout(true)//.setCloneAllBranches(true)
						.setBranchesToClone(Arrays.asList(gitBranch)).setBranch(gitBranch)
						.setGitDir(new File(tempFileLocation));
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