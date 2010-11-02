package br.uff.ic.gardener.client;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

import br.uff.ic.gardener.comm.ComClient;
import br.uff.ic.gardener.comm.ComFactory;
import br.uff.ic.gardener.workspace.Workspace;
import br.uff.ic.gardener.workspace.WorkspaceException;
import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.TransationException;

/**
 * API Client Interface
 * 
 * @author Marcos C�rtes
 */
public class APIClient {
	
	/**
	 * reference to the client aplication
	 */
	private ComClient ComClient = null;

	/**
	 * get APIClient of application
	 * 
	 */
	private ComClient getComClient() {
		return ComClient;
	}
	
	private Workspace workspace = null;
	
	private File path = null;

		private Workspace getWorkspace() throws APIClientException {
		
			if(path == null)
				throw new APIClientException("path não especificado", null);
			if(workspace == null)
			{
				try {
					workspace = new Workspace(path);
				} catch (WorkspaceException e) {
					throw new APIClientException("Não foi possível criar o Workspace", e);
				}
			}
			return workspace;
	}
	
	/**Constructor that Sets Workspace URI and initialize the workspace attribute
	 * @param uriWorkspace
	 *            Workspace path, the name of workspace file*/
	public APIClient(String uriWorkspace) throws APIClientException
	{
		path = new File(uriWorkspace);
		getWorkspace();
	}
	
	public APIClient(File uriWorkspace) throws APIClientException
	{
		path = uriWorkspace;
		getWorkspace();
	}
	
	/**
	 * CheckOut a revision from the serv. It is the simple way.
	 * 
	 * @param items
	 *            the items of revision.
	 * @param revision
	 *            the revision number.
	 * @throw TransationException it throws when the system cannot checkout
	 *        data. It will have a message of the exception
	 */
	//void checkout(Map<String, InputStream> items, RevisionID revision)
	public void checkout(RevisionID revision)throws TransationException{
		
		//get checkout from Communication
		Map<String, InputStream> map = new TreeMap<String, InputStream>();
		try {
			getComClient().checkout(revision, map);
			getWorkspace().checkout(revision, map);
		} catch (TransationException e) {
			throw new TransationException("Error retrieving files from server. ", e);
		} catch (WorkspaceException ew){
			throw new TransationException("Workspace filling error", ew);
		} catch (APIClientException e) {
			throw new TransationException(e.getMessage(), (Exception)e.getCause());
		}
		
		//set checkout to Workspace
		
	}

	/**
	 * Commit a new revision to the serv. it is the simple way.
	 * 
	 * @return the new revision generate
	 * @throws TransationException
	 */
	RevisionID commit() throws TransationException
	{
		return RevisionID.ZERO_REVISION;
		
	}


}
