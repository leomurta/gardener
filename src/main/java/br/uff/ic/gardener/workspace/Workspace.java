package br.uff.ic.gardener.workspace;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import br.uff.ic.gardener.ConfigurationItem;
import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.client.APIClient;
import br.uff.ic.gardener.util.FileHelper;
import br.uff.ic.gardener.util.UtilStream;

public class Workspace {

	/**
	 * Path of the workspace
	 */
	private File path = null;
	
	private String strProjectName = null;
	
	/**
	 * reference to the client aplication
	 */
	private APIClient client = null;
	
	/**
	 * Lista que irá conter as transações especificadas no workspace. Ela nunca altera de tamanho 
	 */
	private ArrayList<CIWorkspaceStatus> listOperations = new ArrayList<CIWorkspaceStatus>();
	
	/**
	 * Lista que receberá as novas operações do workspace para serem gravadas em um commit
	 */
	private LinkedList<CIWorkspaceStatus> listNewOperations = new LinkedList<CIWorkspaceStatus>();
	
	private WorkspaceConfigParser parser = null;
	
	/**
	 * Lista com os itens contidos no workspace. (carregados do arquivo de configuração)
	 */
	private ArrayList<CIWorkspace> listICContent = new ArrayList<CIWorkspace>();
	
	/**
	 * The current revisionID of workspace
	 */
	private RevisionID currentRevision = RevisionID.ZERO_REVISION;
	
	
	private Date checkoutTime = new Date();
	
	private URI servSource = null;

	/**
	 * @deprecated it should use a URI because workspace not should know file access.
	 * @return
	 */
	public File getPath() {
		return path;
	}

	/*
	 * 
	public void setPath(File path) {
		this.path = path;
	}*/

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
	
	private final List<CIWorkspaceStatus> getOperationList()
	{
		return listOperations;
	}
	
	/**
	 * Retorna lista de novas operações a serem manipuladas
	 * @return
	 */
	final List<CIWorkspaceStatus> getNewOperationList()
	{
		return this.listNewOperations;
	}
	
	public String getProjectName()
	{
		return strProjectName;
	}
	
	public void setProjectName(String _str)
	{
		strProjectName = _str;
	}

	/**
	 * Constructor. it needs a path to look up for the configuration files
	 * 
	 * This constructor only load data if the path has the configuration files. If not, it create workspace and wait for save command to write the configuration files in the path.
	 * @param pathOfWorkspace
	 * @param loadWorkspace tryLoadWorkspace in the pathOfWorkspace. Specify it false to create a new workspace without configuration files.
	 * @throws IllegalArgumentException 
	 */
	public Workspace(File pathOfWorkspace) throws IllegalArgumentException, WorkspaceException
	{
		path = pathOfWorkspace;
		
		if (path == null)
			throw new IllegalArgumentException("path cannot be null");
		
		if(!path.isDirectory())
				throw new IllegalArgumentException("path should be a directory");
	
		parser = new WorkspaceConfigParser(this, path);
		
		if(parser.isWorkspaceDir(path))
		{
			try {
				parser.loadProfile(listICContent);
				Collections.sort(listICContent);
			} catch (WorkspaceConfigParserException e) {
				throw new WorkspaceException(null, "Não foi possível carregar as configurações do workspace", e); 
			}
			
			try {
				parser.loadOperations(this.listOperations);
			} catch (WorkspaceConfigParserException e) {
				throw new WorkspaceException(null, "Não foi possível carregar as configurações do workspace", e);
			}
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
	 * Commita as alterações Para isto ele pega os arquivos e diretórios do
	 * diretório corrente, ignorando os .diretórios
	 * 
	 * @throws WorkspaceException
	 */
	public Collection<ConfigurationItem> commit() throws WorkspaceException 
	{
		getClient();
		//File files[] = path.listFiles(new NotInitDotFileFilter());

		return new LinkedList<ConfigurationItem>();
//		Map<String, InputStream> map = new TreeMap<String, InputStream>();
//
//		try {
//			for (File f : files) {
//				InputStream is;
//
//				is = new FileInputStream(f);
//
//				java.io.ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
//
//				try {
//					UtilStream.copy(is, outBuffer);
//
//				} catch (IOException e) {
//					throw new WorkspaceException(f,
//							"Erro ao gravar arquivo no buffer em memória", e);
//				}
//
//				InputStream inputStream = new ByteArrayInputStream(
//						outBuffer.toByteArray());
//				map.put(f.getName(), inputStream);
//			}
//		} catch (FileNotFoundException e) {
//			throw new WorkspaceException(path, "Arquivo não encontrado", e);
//		}
	}

	/**
	 * Implements the completely checkout, generate the workspace It will get
	 * the flow data of serv and will build the Workspace.
	 * 
	 * @throws WorkspaceException
	 */
	public void checkout(RevisionID revision, Collection<ConfigurationItem> list) throws WorkspaceException {
	

		// erase content
		for (File f : path.listFiles(new NotInitDotFileFilter())) {
			f.delete();
		}

		for (ConfigurationItem e : list) {
			//File f = new File(path, e.getStringID());
			File f = null;
			try {
				f = FileHelper.createFile(path, e.getStringID());
			} catch (IllegalArgumentException ee) {
				throw new WorkspaceException(f,
						"Do not create file in repository", ee);
			} catch (IOException eee) {
				throw new WorkspaceException(f,
						"Do not create file in repository", eee);
			}
			try {
				f.createNewFile();
			} catch (IOException e1) {
				throw new WorkspaceException(f,
						"Do not create file in repository", e1);
			}

			try {

				OutputStream out = new FileOutputStream(f);
				UtilStream.copy(e.getItemAsInputStream(), out);

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

	public void removeFiles(Collection<File> listFiles) throws WorkspaceException 
	{
		//número de itens adicionados na lista
		int qtdRemove = 0;
		
		try {
			for(File f: listFiles)
			{
				if(!WorkspaceConfigParser.isConfigFile(getPath(), f))
				{				
					CIWorkspace ci = containItem(f);
					if(ci != null)
					{
						listNewOperations.add(new CIWorkspaceStatus(ci.getURI(), null, Status.REM));
						f.delete();
						qtdRemove ++;
					}else
					{
						while(qtdRemove > 0)
						{
							listNewOperations.removeLast();
							qtdRemove--;
						}
						throw new WorkspaceException(f, String.format("O arquivo %s já foi removido do workspace", f.toString()), null);
					}
				}
			}
		} catch (URISyntaxException e) {
			while(qtdRemove > 0)
			{
				listNewOperations.removeLast();
				qtdRemove--;
			}
			throw new WorkspaceException(null, "Não foi possível interpretar o path do arquivo", e);
		}
	}
	
	public void addFiles(Collection<File> listFiles) throws WorkspaceException 
	{
		//número de itens adicionados na lista nesta operação de add
		int qtdAdd = 0;
		
		try
		{
			for(File f: listFiles)
			{
				if(!WorkspaceConfigParser.isConfigFile(getPath(), f))
				{
					CIWorkspace ci = containItem(f);
					if(ci == null)
					{;
						URI uri = FileHelper.getRelative(getPath(), f);
						listNewOperations.add(new CIWorkspaceStatus(uri, null, Status.ADD));
						qtdAdd ++;
					}else
					{
						while(qtdAdd > 0)
						{
							listNewOperations.removeLast();
							qtdAdd--;
						}
						throw new WorkspaceException(f, String.format("O arquivo %s já está contido no workspace", f.toString()), null);
					}
				}
			}
		} catch (URISyntaxException e) {
			while(qtdAdd > 0)
			{
				listNewOperations.removeLast();
				qtdAdd--;
			}
			throw new WorkspaceException(null, "Não foi possível interpretar o path do arquivo", e);
		}
	}
	
	/**
	 * Verifica se um arquivo já sofreu operação neste workspace
	 * @param f O arquivo que está sendo procurado
	 * @return uma URI para o arquivo que está sendo modificado
	 * @throws URISyntaxException
	 */
	private CIWorkspace containItem(File f) throws URISyntaxException 
	{
		
		URI uri = f.toURI();
		uri = path.toURI().relativize(uri);
		CIWorkspace ci = containItem(this.listOperations, uri);
		if(ci != null)
			return ci;
		
		ci = containItem(this.listNewOperations, uri);
		if(ci != null)
			return ci;
		
		for(CIWorkspace ciFor: this.listICContent)
		{
			if(ciFor.getURI().equals(uri))
				return ciFor;
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
	private static CIWorkspace containItem(Collection<CIWorkspaceStatus> coll, URI uri) throws URISyntaxException
	{
		
		for(CIWorkspace op: coll)
		{
			URI uriColl = op.getURI();
			if(uriColl.equals(uri))
				return op;
		}
		
		return null;
	}

	public void renameFile(File fileSource, String strNewName) throws WorkspaceException
	{
		if(!fileSource.exists())
			throw new WorkspaceException(fileSource, "Arquivo não existe", null);
		
		if(!fileSource.isFile())
			throw new WorkspaceException(fileSource, "Não foi especificado um arquivo válido", null);
		
		try
		{
			File fileNew = new File(getPath(), strNewName);
			fileSource.renameTo(fileNew);
		}catch(Exception e)
		{
			throw new WorkspaceException(fileSource, "Não foi possível renomear para " + strNewName, e);
		}
	}
	
	
	/**
	 * Save workspace content in the config files in the directory
	 * @throws WorkspaceException 
	 */
	public void saveConfig() throws WorkspaceException
	{
		try
		{
			parser.save(getNewOperationList());
		}catch(WorkspaceConfigParserException e)
		{
			throw new WorkspaceException(null, "Error in the save workspace", e);
		}
	}

	public void getStatus(Collection<CIWorkspaceStatus> coll ) throws WorkspaceException
	{
		processStatus(this.getPath(), coll);
	}
	
	/*private boolean[] processStatusFunctionMachine(Collection<CIWorkspaceStatus> coll, CIWorkspace or, CIWorkspaceStatus mod, CIWorkspaceStatus real, boolean [] bReturn)
	{	
		
		if(or.equals(mod) && mod.equals(real))
		{
			
		}else if(or.equals(mod))
		{
			
		}else if (mod.equals(real))
		{
			
		}else if(or.equals(real))
		{
			
		}
		else
		{
			
		}
		
		return bReturn;*/
		/*for(int i = 0; i < bReturn.length; i++)
			bReturn[i] = false;
		
		if(or == null)
			or = CIWorkspaceStatus.NEVER_EQUAL_STATUS;
		
		if(mod == null)
			mod = CIWorkspaceStatus.NEVER_EQUAL_STATUS;
		
		if(real == null)
			real = CIWorkspaceStatus.NEVER_EQUAL_STATUS;
		
		//caso acabou tudo
		if((or == mod) && (or == real) && (or == CIWorkspaceStatus.NEVER_EQUAL_STATUS))
			return bReturn;
		
		if(or.equals(mod))
		{
			if(mod.equals(real))
			{
				//3 equal
				bReturn[0]= bReturn[1] = bReturn[2] = true;
				coll.add(new CIWorkspaceStatus(mod));
			}else
			{
				//or == mod
				bReturn[0]=bReturn[1]=true;
				coll.add(new CIWorkspaceStatus(mod, mod.getStatus().getMissed()));
			}
		}else if(mod.equals(real))
		{
			//mod == real
			bReturn[1]=bReturn[2]=true;
			coll.add(new CIWorkspaceStatus(mod));
		}else if(or.equals(real))
		{
			//or == real
			bReturn[1]=bReturn[2]=true;
			if(real.getDateModified().compareTo(this.getCheckoutTime()) > 0)
			{
				coll.add(new CIWorkspaceStatus(real, Status.VER));
			}else
			{
				coll.add(new CIWorkspaceStatus(real, Status.MOD));
			}
		}else
		{
			//pega o mínimo
			CIWorkspaceStatus ciw = (CIWorkspaceStatus) min((CIWorkspace)or, (CIWorkspace)mod, (CIWorkspace)real);
			if(ciw == or)
			{
				bReturn[0] = true;
				coll.add(new CIWorkspaceStatus(real, Status.VER_MISSED));
			}
			else if(ciw == mod)
			{
				bReturn[1] = true;
				coll.add(new CIWorkspaceStatus(mod, mod.getStatus().getMissed()));
			}
			else
			{
				bReturn[2] = true;
				coll.add(new CIWorkspaceStatus(real, Status.UNVER));
			}
		}
		
		return bReturn;
	}*/
	
	
	private static <T> T pop(Iterator<T> it)
	{
		if(it.hasNext())
			return it.next();
		else
			return null;
	}
	/**
	 * Internal generate status operation
	 * @param currentPath the path inpected now
	 * @param coll the collection witch receives the CIStatus
	 * @throws WorkspaceException 
	 */
	private Collection<CIWorkspaceStatus> processStatus(File currentPath, Collection<CIWorkspaceStatus> coll) throws WorkspaceException {
	
	
		Collection<CIWorkspace> collOriginal = this.getOriginalCIContent();//já ordenado
		
		PriorityQueue<CIWorkspaceStatus> queueNew = new PriorityQueue<CIWorkspaceStatus>();
		
		for(CIWorkspaceStatus ciw: this.getOperationList())
		{
			queueNew.add(ciw);
		}
		
		for(CIWorkspaceStatus ciw: this.getNewOperationList())
		{
			queueNew.add(ciw);
		}
		
		PriorityQueue<CIWorkspaceStatus> queueReal = new PriorityQueue<CIWorkspaceStatus>();
		
		try {
			parser.loadRealICContent(queueReal);
		} catch (WorkspaceConfigParserException e) {
			throw new WorkspaceException(null, "Não foi possível verificar o conteúdo atual do workspace", e);
		}
		
		Iterator<CIWorkspace> itOr = collOriginal.iterator();
		Iterator<CIWorkspaceStatus> itMod = queueNew.iterator();
		Iterator<CIWorkspaceStatus> itReal = queueReal.iterator();
		
		CIWorkspace or = itOr.hasNext()?itOr.next():null;
		CIWorkspaceStatus mod = itMod.hasNext()?itMod.next():null;
		CIWorkspaceStatus real = itReal.hasNext()?itReal.next():null;
		
		while(or != null || mod != null || real != null)
		{
			if(minus(or, mod) && minus(or, real))
			{
				//5 caso, só 1
				coll.add(new CIWorkspaceStatus(or, Status.VER_MISSED));
				or 	= pop(itOr);
			}else if(minus(mod, or) && minus(mod, real))
			{
				//6 caso só 1
				coll.add(new CIWorkspaceStatus(mod, Status.VER_MISSED));
				mod	= pop(itMod);
			}else if(minus(real, or) && minus(real,mod))
			{
				//7 caso só 1
				coll.add(new CIWorkspaceStatus(real, Status.UNVER));
				real= pop(itReal);
			}
			else if(equals(or, mod) && equals(mod, real))
			{
				//1 caso
				
				coll.add(new CIWorkspaceStatus(mod));
				or 	= pop(itOr);
				mod	= pop(itMod);
				real= pop(itReal);
			}else if(equals(or, mod))
			{
				//2 caso
				coll.add(new CIWorkspaceStatus(mod, mod.getStatus().getMissed()));
				or 	= pop(itOr);
				mod	= pop(itMod);
			}else if(equals(mod, real))
			{
				//3 caso
				coll.add(new CIWorkspaceStatus(mod));
				mod	= pop(itMod);
				real= pop(itReal);
			}
			else if(equals(or, real))
			{
				//4 caso
				if(real.getDateModified().compareTo(this.getCheckoutTime()) > 0)
					coll.add(new CIWorkspaceStatus(real, Status.MOD));
				else
					coll.add(new CIWorkspaceStatus(real, Status.VER));
				or	= pop(itOr);
				real= pop(itReal);
			}
		}
		
		
		
		return coll;
		/*CIWorkspace[] listMin = {or, mod,real};
		Arrays.sort(listMin);
		CIWorkspace min = null;
		for(int pos = 0; pos < listMin.length; i++)
		{
			if(listMin[pos] != null)
			{ 
				min = listMin[pos];
				break;
			}				
		}
		
		if(min != null)
		{
			if(min == )
		}*/
		/*
		
		boolean[] bNext = {itOr.hasNext(), itMod.hasNext(), itReal.hasNext()};
		CIWorkspace or = itOr.hasNext()?itOr.next():null;
		CIWorkspaceStatus mod = itMod.hasNext()?itMod.next():null;
		CIWorkspaceStatus real = itReal.hasNext()?itReal.next():null;
		do
		{
			bNext = this.processStatusFunctionMachine(coll, or, mod, real, bNext);
			if(bNext[0])
				or = itOr.next();
			
			if(bNext[1])
				mod = itMod.next();
			
			if(bNext[2])
				real = itReal.next();
				
		}while(bNext[0] == bNext[1] == bNext[2] == false);
		
		*/
	}
	
	/**
	 * null is greater than anything	
	 * @param a
	 * @param b
	 * @return
	 */
	private static boolean minus(CIWorkspace a, CIWorkspace b)
	{
		if(a== null)
		{
				return false;
		}else
		{
			if(b==null)
				return true;
			else
				return a.compareTo(b) < 0;
				
		}
	}
	
	private static boolean equals(CIWorkspace a, CIWorkspace b) {
		if(a == null)
		{
			if(b == null)
				return true;
			else
				return false;
		}else
		{
			if(b != null)
				return a.equals(b);
			else
				return false;
		}
	}

	private Collection<CIWorkspace> getOriginalCIContent()
	{
		return this.listICContent;
	}

	static public FileFilter getNotFileConfigFilter() {
		return WorkspaceConfigParser.getNotFileConfigFilter();
	}

	void clearOperations() 
	{
		listOperations.clear();
		listNewOperations.clear();		
	}
	
	

	/*
	/**
	 * Generate ConfigurationItens representing the new revision configured in the workspace 
	 * @param list
	 *
	public void generateCheckin(List<ConfigurationItem> list) {
		// TODO Auto-generated method stub
		
	}

	**
	 * process operations with reflect a new version
	 *
	public void processOperations() 
	{
	}*/	

}

