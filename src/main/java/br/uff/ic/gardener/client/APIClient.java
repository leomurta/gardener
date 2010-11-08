package br.uff.ic.gardener.client;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


import java.io.FileFilter;

import br.uff.ic.gardener.comm.ComClient;
import br.uff.ic.gardener.comm.ComClientException;
import br.uff.ic.gardener.comm.ComFactory;
import br.uff.ic.gardener.workspace.Workspace;
import br.uff.ic.gardener.workspace.WorkspaceException;
import br.uff.ic.gardener.ConfigurationItem;
import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.TransationException;

/**
 * API Client Interface
 * 
 * @author Marcos Cortes
 */
public class APIClient {
	
	/**
	 * reference to the client aplication
	 */
	private ComClient comClient = null;

	private Workspace workspace = null;
	
	private URI getURIServ()
	{
		return workspace.getServSource();
	}
	
	/**
	 * get APIClient of application
	 * @throws APIClientException 
	 * 
	 */
	private ComClient getComClient() throws APIClientException 
	{
		if(getURIServ() == null && comClient == null)
		{
			throw new APIClientException("Request specify URIServ", null);
		}
		if(comClient == null || !comClient.getURIServ().equals(getURIServ()))
		{
			try {
				comClient = ComFactory.createComClient(getURIServ(), null);
			} catch (Exception e) {
				throw new APIClientException("Não foi possível criar a ComClient", e);
			}
		}
		return comClient;
	}
	
	private Workspace getWorkspace() throws APIClientException 
	{
		if(workspace == null)
			throw new APIClientException("The workspace instance is null", null);
		return workspace;
	}
	
	
	public APIClient(File _uriWorkspace) throws APIClientException
	{
		try
		{
			workspace = new Workspace(_uriWorkspace);
		}catch(WorkspaceException e)
		{
			throw new APIClientException("Can not create Workspace", e);
		}
		
	//	uriServ = workspace.getServSource();
	}
	/**Constructor that Sets Workspace URI and initialize the workspace attribute
	 * @param uriWorkspace
	 *            Workspace path, the name of workspace file*/
	public APIClient(File _uriWorkspace, URI _uriServ) throws APIClientException
	{
		try
		{
			workspace = new Workspace (_uriWorkspace);			
		}catch(WorkspaceException e)
		{
			throw new APIClientException("Can not create Workspace", e);
		}
		getWorkspace();
		workspace.setServSource(_uriServ);		
	}
	
	/**
	 * Return a fileFilter that ignore Workspace Configuration Files
	 * @return
	 * @throws APIClientException 
	 */
	public FileFilter getWorkspaceNotFileConfigFilter() throws APIClientException {
			return getWorkspace().getNotFileConfigFilter();

	}

	
	
	/**
	 * CheckOut a revision from the serv. It is the simple way.
	 * 
	 * @param revision
	 *            the revision number.
	 * @throw TransationException it throws when the system cannot checkout
	 *        data. It will have a message of the exception
	 */
	public void checkout(RevisionID revision, String msg)throws TransationException{
		
		//get checkout from Communication
		List<ConfigurationItem> list = new LinkedList<ConfigurationItem>();
		try {
			getComClient().checkout("", revision, list);//Obtém os itens da conexão 
			getWorkspace().checkout(revision, list);//Popula os itens no Workspace
			getWorkspace().saveConfig();
		} catch (WorkspaceException ew){
			throw new TransationException("Workspace filling error", ew);
		} catch (ComClientException e) {
			throw new TransationException("Error retrieving files from server", e);
		} catch (APIClientException e) {
			throw new TransationException("Error in APIClient ", e);
		}
		
		//set checkout to Workspace
		
	}

	/**
	 * Commit a new revision to the serv. it is the simple way.
	 * 
	 * @return the new revision generate
	 * @throws TransationException
	 * @throws APIClientException 
	 * @throws ComClientException 
	 */
	public RevisionID commit(String msg) throws TransationException
	{
		//List<ConfigurationItem> list = new LinkedList<ConfigurationItem>();
		//getWorkspace().generateCheckin(list);
	//	getComClient().commit("", msg, list);
		//getWorkspace().processOperations();
		return RevisionID.ZERO_REVISION;
	}

	public void addFiles(Collection<File> listFiles) throws APIClientException, WorkspaceException 
	{	
		try
		{
			getWorkspace().addFiles(listFiles);
			getWorkspace().saveConfig();
		}catch(WorkspaceException e)
		{
			throw e;//new APIClientException("Do not add files in the workspace", e);
		}
	}

	public void removeFiles(Collection<File> listFiles) throws APIClientException, WorkspaceException 
	{
		try
		{
			getWorkspace().removeFiles(listFiles);
			getWorkspace().saveConfig();
		}catch(WorkspaceException e)
		{
			throw e;
		}
	}

	public void renameFile(File fileSource, String strNewName) throws APIClientException , WorkspaceException 
	{
		try
		{
			getWorkspace().renameFile(fileSource, strNewName);
			getWorkspace().saveConfig();
		}catch(WorkspaceException e)
		{
			throw e;
		}
		
	}

	/**
	 * Create a project in the serv
	 * @param string
	 * @throws APIClientException 
	 */
	public void init(String strProject) throws APIClientException , WorkspaceException 
	{
		getComClient().init(strProject);
		getWorkspace().setCurrentRevision(RevisionID.ZERO_REVISION);
		try {
			getWorkspace().saveConfig();
		} catch (WorkspaceException e) {
			throw e;
		}
		
	}

	/**
	 * Update the workspace to the last revision
	 */
	public void update() throws TransationException{
		List<ConfigurationItem> list = new LinkedList<ConfigurationItem>();
		try {
			getComClient().checkout("", getComClient().getLastRevision(""), list);
		} catch (ComClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}