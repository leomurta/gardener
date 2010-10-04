package br.uff.ic.gardener.workspace;

import java.io.File;

public class WorkspaceError extends Error {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8725518662190069928L;
	/**
	 * File especificado como fonte do workspace
	 */
	File sourceFile = null;

	/**
	 * COnstrutor
	 * 
	 * @param file
	 *            Determined file for workspace
	 * @param msg
	 *            The message error
	 * @param parent
	 *            the throwable partent
	 */
	public WorkspaceError(File file, String msg, Throwable parent) {
		super(msg, parent);
		sourceFile = file;

	}
}
