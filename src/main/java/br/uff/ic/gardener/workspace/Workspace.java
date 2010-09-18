package br.uff.ic.gardener.workspace;

import java.io.File;

public class Workspace {

	/**
	 * Path of the workspace
	 */
	File path = null;
	
	/**
	 * Constructor. it needs a path to look up for the .gdr file.
	 * @param pathOfWorkspace
	 */
	public Workspace(File pathOfWorkspace)
	{
		if(!path.isDirectory())
			throw new WorkspaceError(pathOfWorkspace, "O caminho :(" + pathOfWorkspace.toString() + ") não é um diretório válido.", null);
		
		path = pathOfWorkspace;
	}
}
