package br.uff.ic.gardener.workspace;

import java.io.File;

/**
 * Uma exceção do workspace
 * @author Marcos
 *
 */
public class WorkspaceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2251516287838447597L;
	/**
	 * File especificado como fonte do workspace
	 */
	File sourceFile = null;
	
	/**
	 * COnstrutor
	 * @param file Determined file for workspace
	 * @param msg The message error
	 * @param parent the throwable partent
	 */
	public WorkspaceException(File file, String msg, Throwable parent)
	{
		super(msg, parent);
		sourceFile = file;
		
	}
}
