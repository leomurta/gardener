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
import br.uff.ic.gardener.util.FileHelper;
import br.uff.ic.gardener.util.NotFileFilter;
import br.uff.ic.gardener.util.UtilStream;

public class Workspace {

	/**
	 * Path of the workspace
	 */
	private File path = null;
	
	/**
	 * Project name of workspace
	 */
	private String strProjectName = null;
	
	/**
	 * reference to the client aplication
	 */
	//private APIClient client = null;
	
	/**
	 * Lista que irá conter as transações especificadas no workspace. Ela nunca altera de tamanho 
	 */
	private ArrayList<CIWorkspaceStatus> listOperations = new ArrayList<CIWorkspaceStatus>();
	
	/**
	 * Lista que receberá as novas operações do workspace para serem gravadas em um commit
	 */
	private LinkedList<CIWorkspaceStatus> listNewOperations = new LinkedList<CIWorkspaceStatus>();
	

	/**
	 * Instance of parser
	 */
	private WorkspaceConfigParser parser = null;
	
	/**
	 * Lista com os itens contidos no workspace. (carregados do arquivo de configuração)
	 */
	private List<CIWorkspace> listICContent = new ArrayList<CIWorkspace>();
	
	/**
	 * The current revisionID of workspace
	 */
	private RevisionID currentRevision = RevisionID.ZERO_REVISION;
	
	/**
	 * Date of checkout currentVersion
	 */
	private Date checkoutTime = new Date();
	
	/**
	 * Serv source of this workspace
	 */
	private URI servSource = null;

	/**
	 * @deprecated it should use a URI because workspace not should know file access.
	 * @return
	 */
	public File getPath() {
		return path;
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
			loadConfig();
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

	/**
	 * Commita as alterações Para isto ele pega os arquivos e diretórios do
	 * diretório corrente, ignorando os .diretórios
	 * 
	 * @throws WorkspaceException
	 */
	public Collection<ConfigurationItem> commit() throws WorkspaceException 
	{
		return new LinkedList<ConfigurationItem>();
	}

	/**
	 * Implements the completely checkout, generate the workspace It will get
	 * the flow data of serv and will build the Workspace.
	 * 
	 * @throws WorkspaceException
	 */
	public void checkout(RevisionID revision, Collection<ConfigurationItem> list) throws WorkspaceException {
	
		listICContent.clear();
		listNewOperations.clear();
		listOperations.clear();
		try {
			this.currentRevision = revision;
			parser.checkout(revision, list, listICContent);
		} catch (WorkspaceConfigParserException e) {
			throw new WorkspaceException("Cannot checkou", e);
		}
	}

	/**
	 * Close the things
	 * @throws WorkspaceException 
	 */
	public void close() throws WorkspaceException
	{
		this.saveConfig();
		//fecha todos os outputstreams
		for(CIWorkspace ci: listICContent)
		{
			if(ci.getInputStream() != null)
				try {
					ci.getInputStream().close();
				} catch (IOException e) {
				}
		}
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
						throw new WorkspaceException(null, String.format("O arquivo %s já foi removido do workspace", f.toString()), null);
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
	
	/**
	 * Do addiction of files
	 * @param listFiles
	 * @throws WorkspaceException
	 */
	public void addFiles(Collection<File> listFiles) throws WorkspaceException 
	{
		addFiles(listFiles, false);
	}
	/**
	 * Do addiction of files
	 * @param listFiles File to add
	 * @param ignoreConflict ignore conflict with others operations
	 */
	public void addFiles(Collection<File> listFiles, boolean ignoreConflict) throws WorkspaceException {
		//número de itens adicionados na lista nesta operação de add
		int qtdAdd = 0;
		for(File f: listFiles)
		{
			if(!WorkspaceConfigParser.isConfigFile(getPath(), f))
			{
				try
				{
					CIWorkspace ci = containItem(f);
					if(ci == null)
					{
						
						URI uri = FileHelper.getRelative(getPath(), f);
						
						listNewOperations.add(new CIWorkspaceStatus(uri, null, Status.ADD));
						qtdAdd ++;
					}else
					{
						if(!ignoreConflict)
						{
							while(qtdAdd > 0)
							{
								listNewOperations.removeLast();
								qtdAdd--;
							}
							throw new WorkspaceException(null, String.format("O arquivo %s já está contido no workspace", f.toString()), null);
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
		}
		
	}


	public void renameFile(File fileSource, String strNewName) throws WorkspaceException
	{
		if(!fileSource.exists())
			throw new WorkspaceException(null, "Arquivo não existe", null);
		
		if(!fileSource.isFile())
			throw new WorkspaceException(null, "Não foi especificado um arquivo válido", null);
		
		try
		{
			File fileNew = new File(getPath(), strNewName);
			fileSource.renameTo(fileNew);
		}catch(Exception e)
		{
			throw new WorkspaceException(null, "Não foi possível renomear para " + strNewName, e);
		}
	}
	
	/**
	 * Load data of workspace
	 * @throws WorkspaceException
	 */
	private void loadConfig() throws WorkspaceException
	{
		listICContent.clear();
		listOperations.clear();
		listNewOperations.clear();
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
	
	/**
	 * Save workspace content in the config files in the directory
	 * @throws WorkspaceException 
	 */
	public void saveConfig() throws WorkspaceException
	{
		try
		{
			parser.save(this.getOriginalCIContent(), getNewOperationList());
			listOperations.addAll(listNewOperations);
			listNewOperations.clear();
		}catch(WorkspaceConfigParserException e)
		{
			throw new WorkspaceException(null, "Error in the save workspace", e);
		}
	}
	
	/**
	 * Salva apagando arquivo de operation
	 * @throws WorkspaceException
	 */
	public void saveConfigRefresh() throws WorkspaceException
	{
		try
		{
			parser.saveWithOutOperation(this.getOriginalCIContent());
			listNewOperations.clear();
			listOperations.clear();
		}catch(WorkspaceConfigParserException e)
		{
			throw new WorkspaceException(null, "Error in the saveConfigRefresh workspace", e);
		}
	}

	public void getStatus(Collection<CIWorkspaceStatus> coll ) throws WorkspaceException
	{
		processStatus(this.getPath(), coll);
	}

	
	/**
	 * Pop item from a iterator. If do not have item in the iterator, return null;
	 * @param <T>
	 * @param it
	 * @return
	 */
	private static <T> T pop(Iterator<T> it)
	{
		if(it.hasNext())
			return it.next();
		else
			return null;
	}
	
	/**
	 * Return the min value of two itens
	 * null is greater than anything	
	 * @param a
	 * @param b
	 * @return
	 */
	private static <T extends Comparable<T>> boolean minus(T a, T b)
	{
		if(a== null)
		{
				return false;
		}else
		{
			if(b==null)
				return true;
			else {
				return a.compareTo(b) < 0;
			}
				
		}
	}
	
	/**
	 * Internal generate status operation
	 * 	
	 * It need list of Original itens, modified items and real itens (non-versioned)
	 * Begin with a iterator for init of tree lists and does a "merge" of them.
	 * for each situation in the merge, the algorith should add a operation in the return list
	 * Algorithm table: (O = original, M = Modified, R = Real)
	 * equal value 	| Operation
	 * OMR 			| pop(O,M,R), add(M), status(M)
	 * OM			| pop(O,M)	, add(M), status(missed(M))
	 * MR			| pop(M,R)	, add(M), status(M)
	 * OR			| pop(O,R)	, add(R), status(Versioned or Modified)
	 * O			| pop(O)	, add(O), status(missed(O))
	 * M			| pop(M)	, add(M), status(missed(M))
	 * R			| pop(R)	, add(R), status(unversioned)
	 * 
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
			}else if(minus(mod, or) && minus((CIWorkspace)mod, real))
			{
				//6 caso só 1
				coll.add(new CIWorkspaceStatus(mod, Status.VER_MISSED));
				mod	= pop(itMod);
			}else if(minus(real, or) && minus((CIWorkspace)real,mod))
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

	public void getCIsToCommit(List<ConfigurationItem> listCI) throws WorkspaceException 
	{
		try {
			this.parser.loadICToCommit(listCI);
		} catch (WorkspaceConfigParserException e) {
			throw new WorkspaceException(null, "Cannot load CI to commit", e);
		}
		
	}

	/**
	 * After commited in comm, the workspace can be update by this method
	 * @param the new revision
	 * @throws WorkspaceException 
	 */
	public void setCommited(RevisionID id) throws WorkspaceException {
		List<CIWorkspaceStatus> listOp = new LinkedList<CIWorkspaceStatus>();
		listOp.addAll(this.getOperationList());
		listOp.addAll(this.getNewOperationList());
		getNewOperationList().clear();
		
		//fazbackup da lista
		List<CIWorkspace> backup = new LinkedList<CIWorkspace>();
		backup.addAll(listICContent);
		
		for(CIWorkspaceStatus op: listOp)
		{
			switch (op.getStatus()) 
			{
			case ADD:
				listICContent.add(op);
				break;
			case REM:
				listICContent.remove(op);
				break;
			case RENAME:
				CIWorkspace ciRenamed = renameCIInCommit(op);
				//
				if(ciRenamed == null)
				{
					//rollback
					listICContent = backup;
					throw new WorkspaceException(op, "Cannot rename item", null);
				}
				break;
			default:
				listICContent = backup;
				throw new WorkspaceException(op, "Status invalid to setCommited operation", null);
			}
		}
		
		this.listNewOperations.clear();
		listOperations.clear();
		
		this.currentRevision = id;
		this.checkoutTime = new Date();//NOW
		try
		{
			this.saveConfigRefresh();
		}catch(WorkspaceException e)
		{
			loadConfig();//force rollback by read config files
		}
	}

	private CIWorkspace renameCIInCommit(CIWorkspaceStatus op)
	{
		Iterator<CIWorkspace> i = listICContent.iterator();
		CIWorkspace current = null;
		boolean achou = false;
		for(current = i.next(); i.hasNext(); current = i.next() )
		{
			if(current.getURI().equals(op.getURI()))
			{
				achou = true;
				break;
			}
		}
		
		if(achou)
		{
			i.remove();
			current = new CIWorkspace(current, op.getOldURI());
			listICContent.add(current);
			return current;
		}
		return null;		
	}
}

