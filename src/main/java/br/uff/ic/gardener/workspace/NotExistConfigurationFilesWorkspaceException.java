package br.uff.ic.gardener.workspace;

import java.io.File;

/**
 * Represents the exception because the path of workspace do not have configuration files
 * @author Marcos
 *
 */
public class NotExistConfigurationFilesWorkspaceException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7003855171362714112L;
	//Path for Workspace
	File path = null;
	
	
	public NotExistConfigurationFilesWorkspaceException (File _path)
	{
		super(String.format("Do not exist workspace configuration files in %s directory", _path.toString()));
		path = _path;
	}
	
	/**
	 * Return the path of Workspace
	 * @return
	 */
	public File getPath()
	{
		return path;
	}
}
