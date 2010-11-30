package br.uff.ic.gardener.client;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


import java.io.FileFilter;

import br.uff.ic.gardener.client.ClientMerge.ClientMergeException;
import br.uff.ic.gardener.comm.ComClient;
import br.uff.ic.gardener.comm.ComClientException;
import br.uff.ic.gardener.comm.ComFactory;
import br.uff.ic.gardener.workspace.CIWorkspace;
import br.uff.ic.gardener.workspace.CIWorkspaceStatus;
import br.uff.ic.gardener.workspace.Status;
import br.uff.ic.gardener.workspace.Workspace;
import br.uff.ic.gardener.workspace.WorkspaceException;
import br.uff.ic.gardener.ConfigurationItem;
import br.uff.ic.gardener.RevisionCommited;
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
	
	private String strUser = "anonymus";
	
	private URI getURIServ()
	{
		return workspace.getServSource();
	}
	
	private ClientMerge merge = null;
	
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
			merge = new ClientMerge();
		}catch(WorkspaceException e)
		{
			throw new APIClientException("Can not create Workspace", e);
		} catch (IOException e) {
			throw new APIClientException("Can not create Workspace, ioexception" +
					"", e);
		}
	}
	/**Constructor that Sets Workspace URI and initialize the workspace attribute
	 * @param uriWorkspace
	 *            Workspace path, the name of workspace file*/
	public APIClient(File _uriWorkspace, URI _uriServ) throws APIClientException
	{
		try
		{
			workspace = new Workspace (_uriWorkspace);	
			merge = new ClientMerge();
		}catch(WorkspaceException e)
		{
			throw new APIClientException("Can not create Workspace", e);
		} catch (IOException e) {
			throw new APIClientException("Cannot create ClientMerge and generate IOException", e);
		}
		workspace.setServSource(_uriServ);		
	}
	
	/**
	 * Return a fileFilter that ignore Workspace Configuration Files
	 * @return
	 * @throws APIClientException 
	 */
	public FileFilter getWorkspaceNotFileConfigFilter() throws APIClientException {
			return Workspace.getNotFileConfigFilter();

	}

	
	
	/**
	 * CheckOut a revision from the serv. It is the simple way.
	 * 
	 * @param revision
	 *            the revision number.
	 * @throw TransationException it throws when the system cannot checkout
	 *        data. It will have a message of the exception
	 */
	public void checkout(RevisionID revision)throws TransationException{
		
		//get checkout from Communication
		List<ConfigurationItem> list = new LinkedList<ConfigurationItem>();
		try {
			revision = getComClient().checkout("", revision, list);//Obtém os itens da conexão 
			getWorkspace().checkout(revision, list);//Popula os itens no Workspace
		} catch (WorkspaceException ew){
			throw new TransationException("Workspace filling error", ew);
		} catch (ComClientException e) {
			throw new TransationException("Error retrieving files from server", e);
		} catch (APIClientException e) {
			throw new TransationException("Error in APIClient ", e);
		}		
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
		List<ConfigurationItem> listCI = new LinkedList<ConfigurationItem>();
		try {
			this.getWorkspace().getCIsToCommit(listCI);
			RevisionID id = this.getComClient().commit(this.getWorkspace().getProjectName(), msg, getUser(), listCI);
			this.getWorkspace().setCommited(id);
			return id;
		} catch (WorkspaceException e) {
			throw new TransationException("Cannot commit", e);
		} catch (APIClientException e) {
			throw new TransationException("Cannot commit: ", e);
		} catch (ComClientException e) {
			throw new TransationException("Cannot commit", e);
		}
	}

	public void addFiles(Collection<File> listFiles) throws APIClientException, WorkspaceException 
	{	
		getWorkspace().addFiles(listFiles);
		
	}

	public void removeFiles(Collection<File> listFiles) throws APIClientException, WorkspaceException 
	{
		try
		{
			getWorkspace().removeFiles(listFiles);
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
		URI uri = getWorkspace().getServSource();
		getWorkspace().reset();
		getWorkspace().setServSource(uri);

	}

	/**
	 * faz merge de dos CIs. Nunca dá exceção pq a política por enquanto é deixar o erro do merge dentro do arquivo
	 * @param ciServ
	 * @param ciWork
	 * @return if it cause conflict
	 */
	private boolean merge(ConfigurationItem ciServ, ConfigurationItem ciWork) {
		try {
			InputStream in = merge.merge(ciServ, ciWork);
			forceClose(ciServ.getItemAsInputStream());
			forceClose(ciWork.getItemAsInputStream());
			ConfigurationItem ci = new ConfigurationItem(ciWork.getUri(), in, ciServ.getRevision());
			getWorkspace().replaceCI(ci);
			
			return merge.lastConflict();
		} catch (WorkspaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientMergeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true; 
	}
	
	
	/**
	 * S W B ->Merge 3Way
	 * S W _ ->Merge 2Way
	 * _ W B ->Nothing
	 * S _ B ->Conflito, ws removeu um arquivo
	 * S _ _ ->Adiciona S
	 * _ W _ ->Faz anda, W deve ser um add
	 * _ _ B ->inconsistente, deveria ter informação de remoção em W.
	 * @param collConflict collection of conflicts  
	 */
	public void update(Collection<Conflict> collConflict) throws APIClientException
	{
		List<ConfigurationItem> listS = new LinkedList<ConfigurationItem>(); //new version in the Serv
		List<ConfigurationItem> listO = new LinkedList<ConfigurationItem>(); //Original version
		
		List<CIWorkspaceStatus> listW = new LinkedList<CIWorkspaceStatus>(); //modifications in the workspace
		
		try {
			getComClient().checkout("", RevisionID.LAST_REVISION, listS);
			getComClient().checkout("", getWorkspace().getCurrentRevision(), listO);
			getWorkspace().getStatus(listW);
			Collections.sort(listS);
			Collections.sort(listO);
			Collections.sort(listW);
		} catch (ComClientException e) {
			throw new APIClientException("Cannot get versions to update", e);
		} catch (WorkspaceException e) {
			throw new APIClientException("Cannot get workspace status to update", e);
		}
		
		Iterator<ConfigurationItem> itS = listS.iterator();
		Iterator<CIWorkspaceStatus> itW = listW.iterator();
		Iterator<ConfigurationItem> itO = listO.iterator();
		ConfigurationItem ciS = null;
		ConfigurationItem ciO = null;
		CIWorkspaceStatus ciW = null;
		
		
			ciS = next(itS);		
			ciO = next(itO);		
			ciW = next(itW);
		
		while(ciS != null || ciO  != null && ciW != null)
		{
			
			int S_O = compareCI(ciS, ciO);
			
			int S_W = compareCI(ciS, ciW);
			
			int W_O = compareCI(ciO, ciW);
			
			if((0 == S_O)  && (0 == W_O) &&  (0 == S_W)) //igual
			{
				//faz 3Way merge
				boolean conflict = merge(ciS,ciW, ciO);
				if(conflict)
				{
					collConflict.add(new Conflict(ciS.getUri(), ciW.getURI()));
				}
				ciS = next(itS);		
				ciO = next(itO);		
				ciW = next(itW);
				
			}else if(0 == S_O)
			{	
				throw new APIClientException("Not implemented", null);
			}else if(0 == S_W)
			{
				boolean conflict = merge(ciS,ciW);
				if(conflict)
				{
					collConflict.add(new Conflict(ciS.getUri(), ciW.getURI()));
				}
				ciS = next(itS);		
				ciW = next(itW);
			}else if(0 == W_O)
			{
				throw new APIClientException("Not implemented", null);
			}else
			{ 
				//os três diferentes
				if(S_W < 0 && S_O < 0) //S<W && S<O S menor
				{
					//novo no servidor
					try {
						getWorkspace().createAndAddFile(ciS.getUri(), ciS.getItemAsInputStream());
					} catch (WorkspaceException e) {
						throw new APIClientException("Problem in create file to Update", e);
					}
					finally
					{
						forceClose(ciS.getItemAsInputStream());
					}
					ciS = next(itS);
				}else if(S_W > 0 && W_O < 0) //W<O && W<S W menor
				{
					//adicionado
					ciW = next(itW);
				}else if(S_O > 0 && W_O > 0)//O<S && O<W O menor
				{
					//inconsistente!
					throw new APIClientException("Inconsistent state: Cannnot exist only original file", null);
					//ciO = next(itO);
				}
				
			}
		}
	}
	
	private boolean merge(ConfigurationItem ciS, CIWorkspaceStatus ciW) {
		boolean conflict = false;
		try {
			switch(ciW.getStatus())
			{
			
				case ADD:
				case MOD:
				case VER:
				case UNVER:
				//case RENAME:
					Reader inS = new InputStreamReader(ciS.getItemAsInputStream());
					Reader inW = new InputStreamReader(getWorkspace().getCIStream(ciW));
					InputStream in = merge.merge(inS, inW);
					forceClose(inS);
					forceClose(inW);
					getWorkspace().replaceCI(ciW, in);
					conflict = merge.lastConflict();
				break;
				default:
					conflict = true;
			}			
		} catch (ClientMergeException e) {
			e.printStackTrace();
		} catch (APIClientException e) {
			e.printStackTrace();
		} catch (WorkspaceException e) {
			e.printStackTrace();
		}
		return conflict; 
	}

	private static int compareCI(ConfigurationItem a, ConfigurationItem b)
	{
		if(a == null)
		{
			if(b == null)
				return 0;
			else
				return Integer.MAX_VALUE;
		}else
		{
			if(b == null)
				return Integer.MIN_VALUE;
		}
		String sA = a.getStringID();
		String sB = b.getStringID();
		return sA.compareTo(sB);	
	}
	
	private static int compareCI(ConfigurationItem a, CIWorkspaceStatus b)
	{
		if(a == null)
		{
			if(b == null)
				return 0;
			else
				return Integer.MAX_VALUE;
		}else
		{
			if(b == null)
				return Integer.MIN_VALUE;
		}
		
		String sA = a.getStringID();
		String sB = b.getStringID();
		return sA.compareTo(sB);		
	}
	
	private static <T> T next(Iterator<T> it)
	{
		if(it.hasNext())
			return it.next();
		else
			return null;
	}
	/**
	 * S = W = B -> Merge do arquivo
	 * S = W < B -> Conflito no arquivo pq o servidor tem um artefato igual a um criado no ws
	 * S > W = B -> Remove arquivo no W
	 * S > W > B -> Mesmo arquivo removido no S e no W. Deixar como estar
	 * S > W < B -> Novo arquivo no WS. Manter
	 * S < W < B -> Novo arquivo no servidor. Adicionar.
	 * Update the workspace to the last revision
	 
	public void update(Collection<Conflict> conflicts) throws TransationException{
		List<ConfigurationItem> listServ = new LinkedList<ConfigurationItem>();
		List<ConfigurationItem> listWork = new LinkedList<ConfigurationItem>();
		try {
			getComClient().checkout("", RevisionID.LAST_REVISION, listServ);
			getWorkspace().getCIsToCommit(listWork);
			Collections.sort(listServ);
			Collections.sort(listWork);
			
			Iterator<ConfigurationItem> is = listServ.iterator();
			Iterator<ConfigurationItem> iw = listWork.iterator();
			
			ConfigurationItem ciServ = null;
			ConfigurationItem ciWork = null;
			ciServ = is.next();
			ciWork = iw.next();
			while(is.hasNext() && iw.hasNext())
			{				
				//faz o merge
				
				String strServ = ciServ.getUri().getPath();
				String strWork = ciWork.getUri().getPath();
				
				final int diff = strServ.compareTo(strWork);
				if(0 == diff)//mesmo item
				{
					boolean conflict = merge(ciServ,ciWork);
					if(conflict)
					{
						conflicts.add(new Conflict(ciServ.getUri(), ciWork.getUri()));
					}
					ciServ =is.next();
					ciWork = iw.next();
				}else if (0 < diff) //strServ é menor. Grava ele no disco
				{
					addCIWorkspace(ciServ);
					ciServ =is.next();
					
				}else //strServ é maior. faz nada, mantém o do ws
				{
					ciWork = iw.next();
				}
			}
			
			//atribui a nova revisão ao workspace
			workspace.setCurrentRevision(getComClient().getLastRevision(""));
			
		} catch (ComClientException e) {
			throw new TransationException("Cannot realize update transation", e);
		} catch (APIClientException e) {
			throw new TransationException("Cannot realize update transation", e);
		} catch (WorkspaceException e) {
			e.printStackTrace();
		}
		
	}*/

	/**
	 * Return the workspace CIs without modifications by user
	 * @param listBase
	 * @throws APIClientException 
	 * @throws WorkspaceException 
	
	private void getUnmodifiedWorkspace(List<ConfigurationItem> listBase) throws WorkspaceException, APIClientException {
		getWorkspace().getUnmodifiedWorkspace(listBase);
	} 

	private void addCIWorkspace(ConfigurationItem ci) throws WorkspaceException, APIClientException
	{
		getWorkspace().addNewCI(ci);
	}
	*/
	

	/**
	 * faz merge de dos CIs. Nunca dá exceção pq a política por enquanto é deixar o erro do merge dentro do arquivo
	 * @param ciServ
	 * @param ciWork
	 * @return if it cause conflict
	 */
	private boolean merge(ConfigurationItem ciS, CIWorkspaceStatus ciWork, ConfigurationItem ciBase) {
		boolean conflict = false;
		try {
			switch(ciWork.getStatus())
			{
			
				case ADD:
				case MOD:
				case VER:
				case UNVER:
				//case RENAME:
					InputStream inS = ciS.getItemAsInputStream();
					InputStream inO = ciBase.getItemAsInputStream();
					InputStream inW = getWorkspace().getCIStream(ciWork);
					InputStream in = merge.merge(inS, inW, inO);
					forceClose(inS);
					forceClose(inO);
					forceClose(inW);
					getWorkspace().replaceCI(ciWork, in);
					conflict = merge.lastConflict();
				break;
				default:
					conflict = true;
			}			
		} catch (ClientMergeException e) {
			e.printStackTrace();
		} catch (APIClientException e) {
			e.printStackTrace();
		} catch (WorkspaceException e) {
			e.printStackTrace();
		}
		return conflict; 
	}

	public void status(Collection<CIWorkspaceStatus> coll) throws WorkspaceException, APIClientException {
		getWorkspace().getStatus(coll);
		
	}

	public void forceClose() throws WorkspaceException, APIClientException 
	{
		forceClose(workspace);
		forceClose(comClient);
	}
	
	private static void forceClose(Closeable c)
	{
		if(c!= null)
			try {
				c.close();
			} catch (IOException e) {
			}
	}
	
	/**
	 * generateLog of the server
	 * @param list list of revisions logged
	 * @param revision the first revision. if equal to null, initiate at Revision 1.
	 * @param lastRevision the last revision. if equal null, initiate at last Revision
	 */
	public void generateLog(Collection<RevisionCommited> coll,
			RevisionID firstRevision, RevisionID lastRevision) throws APIClientException, ComClientException
	{
			getComClient().generateLog(coll, firstRevision, lastRevision);
	}

	public void setUser(String _strUser) {
		strUser = _strUser;
	}
	
	public final String getUser()
	{
		return strUser;
	}

}