/**
 * 
 */
package com.barclays.card.bot.exception;

import org.eclipse.jgit.api.errors.GitAPIException;

/**
 * @author Rahul
 *
 */
public class GitException extends GitAPIException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3597243598437265890L;

	public GitException(String message) {
		super(message);
	}

	public GitException(String message, Throwable cause) {
		super(message, cause);
	}

}
