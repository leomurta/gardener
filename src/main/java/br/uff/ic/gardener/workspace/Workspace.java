package br.uff.ic.gardener.workspace;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.TransationException;
import br.uff.ic.gardener.client.APIClient;
import br.uff.ic.gardener.util.UtilStream;
import br.uff.ic.gardener.workspace.WorkspaceOperation.Operation;

public class Workspace {

	/**
	 * Path of the workspace
	 */
	private File path = null;
	
	/**
	 * reference to the client aplication
	 */
	private APIClient client = null;
	
	/**
	 * Lista que irá conter as transações especificadas no workspace. Ela nunca altera de tamanho 
	 */
	private ArrayList<WorkspaceOperation> listOperations = new ArrayList<WorkspaceOperation>();
	
	/**
	 * Lista que receberá as novas operações do workspace para serem gravadas em um commit
	 */
	private LinkedList<WorkspaceOperation> listNewOperations = new LinkedList<WorkspaceOperation>();
	
	private WorkspaceConfigParser parser = null;
	
	/**
	 * Lista com os itens contidos no workspace. (carregados do arquivo de configuração)
	 */
	private ArrayList<URI> listICContent = new ArrayList<URI>();
	
	/**
	 * The current revisionID of workspace
	 */
	private RevisionID currentRevision = RevisionID.ZERO_REVISION;
	
	private Date checkoutTime = new Date();
	
	private URI servSource = null;

	public File getPath() {
		return path;
	}

	public void setPath(File path) {
		this.path = path;
	}

	/**
	 * get APIClient of application
	 * 
	 */
	private APIClient getClient() {
		return client;
	}


	public Date getCheckoutTime() {
		return checkoutTime;
	}

	public void setCheckoutTime(Date checkoutTime) {
		this.checkoutTime = checkoutTime;
	}

	public RevisionID getCurrentRevision() {
		return currentRevision;
	}

	public void setCurrentRevision(RevisionID revision) {
		currentRevision = revision;
	}
	
	public final List<WorkspaceOperation> getOperationList()
	{
		//carrega a lista de algum lugar
		
		return listOperations;
	}
	
	public List<WorkspaceOperation> getNewOperations()
	{
		return listNewOperations;
	}

	/**
	 * Constructor. it needs a path to look up for the .gdr file.
	 * 
	 * @param pathOfWorkspace
	 * @throws WorkspaceException 
	 */
	public Workspace(File pathOfWorkspace, APIClient _client) throws WorkspaceException {
		if (!path.isDirectory())
			throw new WorkspaceError(pathOfWorkspace, "the path:("
					+ pathOfWorkspace.toString()
					+ ") is not a valid directory.", null);

		client = _client;

		if (client == null)
			throw new WorkspaceError(pathOfWorkspace,
					"the APIClient is not valid (is null)", null);

		path = pathOfWorkspace;
		
		parser = new WorkspaceConfigParser(this, path);
		try {
			parser.loadProfile(listICContent);
		} catch (WorkspaceConfigParserException e) {
			throw new WorkspaceException(null, "Não foi possível carregar as configurações do workspace", e); 
		}
		
		try {
			parser.loadOperations(this.listOperations);
		} catch (WorkspaceConfigParserException e) {
			throw new WorkspaceException(null, "Não foi possível carregar as configurações do workspace", e);
		}		
	}

	private class NotInitDotFileFilter implements FileFilter {

		@Override
		public boolean accept(File arg0) {
			String temp = arg0.getName();
			return temp.length() == 0 || temp.charAt(0) != '.';
		}

	}

	/**
	 * Commita as alterações Para isto ele pega os arquivos e diret�rios do
	 * diret�rio corrente, ignorando os .diret�rios
	 * 
	 * @throws WorkspaceException
	 */
	public void commit() throws WorkspaceException {
		File files[] = path.listFiles(new NotInitDotFileFilter());

		Map<String, InputStream> map = new TreeMap<String, InputStream>();

		try {
			for (File f : files) {
				InputStream is;

				is = new FileInputStream(f);

				java.io.ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();

				try {
					UtilStream.copy(is, outBuffer);

				} catch (IOException e) {
					throw new WorkspaceException(f,
							"Erro ao gravar arquivo no buffer em mem�ria", e);
				}

				InputStream inputStream = new ByteArrayInputStream(
						outBuffer.toByteArray());
				map.put(f.getName(), inputStream);
			}
		} catch (FileNotFoundException e) {
			throw new WorkspaceException(path, "Arquivo n�o encontrado", e);
		}

		try {
			getClient().commit(map);
		} catch (TransationException e) {
			throw new WorkspaceException(this.path,
					"não foi poss�vel enviar as alterações para o servidor.", e);
		}
	}

	/**
	 * Implements the completely checkout, generate the workspace It will get
	 * the flow data of serv and will build the Workspace.
	 * 
	 * @throws WorkspaceException
	 */
	public void checkout(RevisionID revision) throws WorkspaceException {
		Map<String, InputStream> map = new TreeMap<String, InputStream>();
		try {
			getClient().checkout(map, revision);
		} catch (TransationException e) {
			throw new WorkspaceException(this.path,
					"n�o foi poss�vel resgatar as altera��es do o servidor.", e);
		}

		// erase content
		for (File f : path.listFiles(new NotInitDotFileFilter())) {
			f.delete();
		}

		for (Map.Entry<String, InputStream> e : map.entrySet()) {
			File f = new File(path, e.getKey());
			try {
				f.createNewFile();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				throw new WorkspaceException(f,
						"Do not create file in repository", e2);
			}

			try {

				OutputStream out = new FileOutputStream(f);
				UtilStream.copy(e.getValue(), out);

			} catch (IOException e1) {
				throw new WorkspaceException(f,
						"Do not copy data from repository", e1);
			}

		}

	}

	/**
	 * Close the things
	 */
	public void close() {
		// fecha o que venha a ser necess�rio fechar
	}

	public void setServSource(URI servSource) {
		this.servSource = servSource;
	}

	public URI getServSource() {
		return servSource;
	}

	public void addFiles(Collection<File> listFiles) throws WorkspaceException 
	{
		for(File f: listFiles)
		{
			try {
				URI uri = containItem(f);
				if(uri == null)
				{
					listNewOperations.add(new WorkspaceOperation(WorkspaceOperation.Operation.ADD_FILE, f.toString() ));
				}else
				{
					throw new WorkspaceException(f, "O arquivo %s já está contido no workspace", null);
				}
			} catch (URISyntaxException e) {
				throw new WorkspaceException(f, "Não foi possível interpretar o path do arquivo", e);
			}
		}
	}
	
	/*private URI getRelativeURI(URI absURI)
	{
		URI sourceURI = path.toURI();
		return sourceURI.relativize(absURI);
	}*/
	
	
	private URI containItem(File f) throws URISyntaxException 
	{
		
		URI uri = f.toURI();
		uri = uri.relativize(path.toURI());
		URI wo = containItem(this.listOperations, uri);
		if(wo != null)
			return wo;
		
		wo = containItem(this.listNewOperations, uri);
		if(wo != null)
			return wo;
		
		for(URI uriIC: this.listICContent)
		{
			if(uriIC.equals(uri))
				return uriIC;
		}
		return null;
		
		
	}
	
	/**
	 * Return a operation of add in container coll
	 * @param coll Container
	 * @param uri Relative URI
	 * @return the operation that references the URI.
	 * @throws URISyntaxException 
	 */
	private static URI containItem(Collection<WorkspaceOperation> coll, URI uri) throws URISyntaxException
	{
		
		for(WorkspaceOperation op: coll)
		{
			if((op.getOperation() == Operation.ADD_FILE) && (op.getParamQtd() <= 0))
			{				
				String str = op.getParamAt(0);
				
				URI uriColl = new URI(str);
				if(uriColl.equals(uri))
					return uriColl;
			}
		}
		
		return null;
	}
	

}

