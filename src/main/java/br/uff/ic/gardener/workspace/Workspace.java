package br.uff.ic.gardener.workspace;

import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.util.Queue;

import br.uff.ic.gardener.ConfigurationItem;
import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.util.FileHelper;
import br.uff.ic.gardener.util.UtilStream;

public class Workspace implements Closeable{

	/**
	 * Project name of workspace
	 */
	private String strProjectName = null;

	/**
	 * Lista que irá conter as transações especificadas no workspace. Ela não deveria alterar de tamanho frequentemente 
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

	private WorkspaceConfigParser getParser()
	{
		return parser;
	}
	
	public final Collection<CIWorkspaceStatus> getOperations()
	{
		return listOperations;
	}
	
	public void addContent(Collection<? extends CIWorkspace> content) {
		listICContent.addAll(content);
		Collections.sort(listICContent);
	}
	

	public void addOldOperation(LinkedList<CIWorkspaceStatus> list) {
		listOperations.addAll(list);
	}
	
	public void addNewOperation(CIWorkspaceStatus ci)
	{
		listNewOperations.add(ci);
	}
	
	public final Collection<CIWorkspaceStatus> getNewOperations()
	{
		return listNewOperations;
	}
	
	/**
	 * return the ci versioneds
	 * @return
	 */
	public final Collection<CIWorkspace> getWorkspaceVersionedContent()
	{
		return listICContent;
	}
	
	/**
	 * @return
	 */
	private File getPath() {
		return getParser().getPath();
	}
	
	public Date getCheckoutTime() {
		return checkoutTime;
	}

	public void setCheckoutTime(Date _checkoutTime) {
		this.checkoutTime = _checkoutTime;
	}

	public RevisionID getCurrentRevision() {
		return currentRevision;
	}

	public void setCurrentRevision(RevisionID revision) {
		currentRevision = revision;
	}
	
	
	public String getProjectName()
	{
		return strProjectName;
	}
	
	public void setProjectName(String _str)
	{
		strProjectName = _str;
	}

	public void setServSource(URI _servSource) 
	{
		this.servSource = _servSource;
	}

	public URI getServSource() {
		return servSource;
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
	//	path = pathOfWorkspace;
		
		if (pathOfWorkspace == null)
			throw new IllegalArgumentException("path cannot be null");
		
		if(!pathOfWorkspace.isDirectory())
				throw new IllegalArgumentException("path should be a directory");
	try {
		parser = new WorkspaceConfigParser(this, pathOfWorkspace);
		
		} catch (WorkspaceConfigParserException e) {
			parser = null;
			this.reset();
			throw new WorkspaceException("Cannot load config in parser", e);
		}
		
	}

	/*@SuppressWarnings("unused")
	private class NotInitDotFileFilter implements FileFilter {

		@Override
		public boolean accept(File arg0) {
			String temp = arg0.getName();
			return temp.length() == 0 || temp.charAt(0) != '.';
		}

	}*/
	
	
	/**
	 * Verifica se um arquivo já sofreu operação neste workspace
	 * @param f O arquivo que está sendo procurado
	 * @return uma URI para o arquivo que está sendo modificado
	 * @throws URISyntaxException
	 * @Deprecated
	 */
	private CIWorkspace containItem(File f) throws URISyntaxException 
	{
		
		URI uri = f.toURI();
		uri = getPath().toURI().relativize(uri);
		CIWorkspace ci = containItem(this.listOperations, uri);
		if(ci != null)
			return ci;
		
		ci = containItem(this.listNewOperations, uri);
		if(ci != null)
			return ci;
		
		for(CIWorkspace ciFor: this.listICContent)
		{
			if(ciFor.getStringID().equals(uri.getPath()))
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
	 * @Deprecated
	 */
	private static CIWorkspace containItem(Collection<? extends CIWorkspace> coll, URI uri) throws URISyntaxException
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
	 * Close the things
	 */
	@Override
	public void close() throws IOException
	{
		reset();
	}

	

	/**
	 * Implements the completely checkout, generate the workspace It will get
	 * the flow data of serv and will build the Workspace.
	 * 
	 * @throws WorkspaceException
	 */
	public void checkout(RevisionID revision, Collection<ConfigurationItem> list) throws WorkspaceException {
		URI serv = getServSource();
		reset();
		setServSource(serv);
		try {
			setCurrentRevision(revision);
			// erase content
			File[] listContent = getPath().listFiles();
			for(File f: listContent)
			{
				FileHelper.deleteDirTree(f);
			}

			//save content
			for (ConfigurationItem e : list) {
				File f = null;
					try {
					f = FileHelper.createFile(this.getPath(), e.getStringID());
				} catch (IllegalArgumentException ee) {
					throw new WorkspaceConfigParserException("Do not create file in repository",f.toString(), ee);
				} catch (IOException eee) {
					throw new WorkspaceConfigParserException("Do not create file in repository", f.toString(), eee);
				}
				try {
					f.createNewFile();
				} catch (IOException e1) {
					throw new WorkspaceConfigParserException("Do not create file in repository", f.toString(), e1);
				}

				try {

					OutputStream out = new FileOutputStream(f);
					UtilStream.copy(e.getItemAsInputStream(), out);
					out.close();
				
				} catch (IOException e1) {
					throw new WorkspaceConfigParserException("Do not copy data from repository", f.toString(), e1);
				}
				
				CIWorkspace ci = new CIWorkspace(e.getUri(), new Date());
				this.listICContent.add(ci);
			}
			Collections.sort(listICContent);
			this.setCheckoutTime(new Date());
			this.saveConfig();
		} catch (WorkspaceConfigParserException e) {
			reset();
			throw new WorkspaceException("Cannot checkout", e);
		}
	}

	/*private File getPathUnmodified() {
		return new File(getPath(), ".unmodified");
	}*/

	private 	static Collection<File> tempColl = new ArrayList<File>();
	public void removeFile(File file) throws WorkspaceException 
	{
		synchronized(tempColl)
		{
			tempColl.clear();
			tempColl.add(file);
			removeFiles(tempColl);
		}
	}
	
	public void removeFiles(Collection<File> listFiles) throws WorkspaceException 
	{
		//número de itens adicionados na lista
		int qtdRemove = 0;
		
		try {
			for(File f: listFiles)
			{
				if(!getParser().isConfigFile(f))
				{				
					CIWorkspace ci = containItem(f);
					if(ci != null)
					{
						if(ci instanceof CIWorkspaceStatus && ((CIWorkspaceStatus)ci).getStatus() == Status.REM)
						{
							throw new WorkspaceException(null, String.format("O o arquivo %s já foi removido do Workspace", f.toString()), null);
						}
						
						if(f.delete())
							listNewOperations.add(new CIWorkspaceStatus(FileHelper.getRelative(getPath(), f), Status.REM));
						else
						{
							while(qtdRemove > 0)
							{
								listNewOperations.removeLast();
								qtdRemove--;
							}
							throw new WorkspaceException(null, String.format("Não foi possível remover o arquivo %s", f.toString()), null);
						}
							
						qtdRemove ++;
					}else
					{
						while(qtdRemove > 0)
						{
							listNewOperations.removeLast();
							qtdRemove--;
						}
						throw new WorkspaceException(null, String.format("O arquivo %s não existe no workspace", f.toString()), null);
					}
				}
			}
			saveConfig();
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
			if(!getParser().isConfigFile(f))
			{
				try
				{
					CIWorkspace ci = containItem(f);
					if(ci == null)
					{
						
						URI uri = FileHelper.getRelative(getPath(), f);
						
						listNewOperations.add(new CIWorkspaceStatus(uri, Status.ADD));
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
		
		saveConfig();
		
	}


	public void renameFile(File fileSource, String strNewName) throws WorkspaceException
	{		
		try
		{
			CIWorkspace ci = containItem(fileSource);
			
	//		containItem(listICContent, uri)
			
			if(ci == null)
			{
				throw new WorkspaceError(fileSource, "renamed file has have a operation: " + ci.toString(), null);
			}
			
			 if(!fileSource.exists())
				throw new WorkspaceConfigParserException("Arquivo não existe", "", null);
			
			if(!fileSource.isFile())
				throw new WorkspaceConfigParserException("Não foi especificado um arquivo válido", "", null);
			
			File fileNew = new File(getPath(), strNewName);
			fileSource.renameTo(fileNew);		
			
			listNewOperations.add(new CIWorkspaceStatus(getParser().getRelativeURI(fileSource.toURI()), Status.RENAME));
			saveConfig();
		}catch(WorkspaceConfigParserException e)
		{
			throw new WorkspaceException(null, "Não foi possível renomear para " + strNewName, e);
		} catch (URISyntaxException e) {
			throw new WorkspaceException(null, "Cannot get URI of a file " + strNewName, e);
		}
	}
	
	/**
	 * Load data of workspace
	 * @deprecated
	 * @throws WorkspaceException
	 */
	/*private void loadConfig() throws WorkspaceException
	{
		listICContent.clear();
		listOperations.clear();
		listNewOperations.clear();
		try {
			getParser().loadProfile(listICContent);
			Collections.sort(listICContent);
		} catch (WorkspaceConfigParserException e) {
			throw new WorkspaceException(null, "Não foi possível carregar as configurações do workspace", e); 
		}
		
		try {
			parser.loadOperations(this.listOperations);
		} catch (WorkspaceConfigParserException e) {
			throw new WorkspaceException(null, "Não foi possível carregar as configurações do workspace", e);
		}
	}*/
	
	/**
	 * Save workspace content in the config files in the directory
	 * @throws WorkspaceException 
	 * @Deprecated
	 */
	public void saveConfig() throws WorkspaceException
	{
		try
		{
			parser.save();
			listOperations.addAll(listNewOperations);
			listNewOperations.clear();
		}catch(WorkspaceConfigParserException e)
		{
			throw new WorkspaceException(null, "Error in the save workspace", e);
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
	private void loadRealICContent(Collection<CIWorkspaceStatus> collReal) throws WorkspaceConfigParserException {
		Queue<File> list = new LinkedList<File>();
		list.add(getPath());
		while(list.size() > 0)
		{
			File current = list.remove();
			File[] files = current.listFiles(WorkspaceConfigParser.getNotFileConfigFilter());
			for(File f: files)
			{
				if(f.isDirectory())
					list.add(f);
				else if(f.isFile())
				{
				//	try {
						collReal.add(
								new CIWorkspaceStatus(
										FileHelper.getRelative(getPath(), f),
										/*new FileInputStream(f),*/
										Status.UNVER,
										new Date(f.lastModified()),
										null
									)
								);
				/*	} catch (FileNotFoundException e) {
						throw new WorkspaceConfigParserException("Can not open a file", f.toString(), e);
					}*/
				}
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
		
		//PriorityQueue<CIWorkspaceStatus> queueNew = new PriorityQueue<CIWorkspaceStatus>();
		List<CIWorkspaceStatus> queueNew = new ArrayList<CIWorkspaceStatus>(this.getOperations().size() + this.getNewOperations().size());
		
		for(CIWorkspaceStatus ciw: this.getOperations())
		{
			queueNew.add(ciw);
		}
		
		for(CIWorkspaceStatus ciw: this.getNewOperations())
		{
			queueNew.add(ciw);
		}
		Collections.sort(queueNew);
		
		//PriorityQueue<CIWorkspaceStatus> queueReal = new PriorityQueue<CIWorkspaceStatus>();
		List<CIWorkspaceStatus> queueReal = new ArrayList<CIWorkspaceStatus>(queueNew.size());
		
		try {
			loadRealICContent(queueReal);
			Collections.sort(queueReal);
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
				if(mod.getStatus() == Status.REM)
					coll.add(mod);
				else
					coll.add(new CIWorkspaceStatus(mod, mod.getStatus().getMissed()));
				
				mod	= pop(itMod);
			}else if(minus(real, or) && minus((CIWorkspace)real,mod))
			{
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
				if(mod.getStatus() == Status.REM)
					coll.add(mod);
				else
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

	private void loadICToCommit(Collection<ConfigurationItem> collDest) throws WorkspaceConfigParserException
	{
		Queue<File> list = new LinkedList<File>();
		list.add(getPath());
		while(list.size() > 0)
		{
			File current = list.remove();
			File[] files = current.listFiles(WorkspaceConfigParser.getNotFileConfigFilter());
			for(File f: files)
			{
				if(f.isDirectory())
					list.add(f);
				else if(f.isFile())
				{
					try {
						collDest.add(
								new ConfigurationItem( 
										FileHelper.getRelative(getPath(), f),
										new FileInputStream(f),
										RevisionID.NEW_REVISION
									)
								);
					} catch (FileNotFoundException e) {
						throw new WorkspaceConfigParserException("Can not open a file", f.toString(), e);
					}
				}
			}
		}
	}
		
	public void getCIsToCommit(List<ConfigurationItem> listCI) throws WorkspaceException 
	{
		try {
			loadICToCommit(listCI);
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
		listOp.addAll(this.getOperations());
		listOp.addAll(this.getNewOperations());
		listNewOperations.clear();
		
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
				CIWorkspace ciRenamed = null;
				try
				{
					ciRenamed = renameCIInCommit(op);
				}catch(WorkspaceException e)
				{
					listICContent = backup;
					throw e;
				}
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
		Collections.sort(listICContent);
		
		this.listNewOperations.clear();
		listOperations.clear();
		
		this.currentRevision = id;
		setCheckoutTime(new Date());//NOW
		try
		{
			getParser().save();
			getParser().clearOperationData();
		}catch(WorkspaceConfigParserException e)
		{
			throw new WorkspaceException("Não foi possível salvar configurações do workspace durante o commit", e);
		}
	}

	private CIWorkspace renameCIInCommit(CIWorkspaceStatus op) throws WorkspaceException
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
	
	public void reset()
	{
		checkoutTime = new Date();
		currentRevision = RevisionID.NEW_REVISION;
	//	closeAll(listICContent);
		listICContent.clear();
		
	//	closeAll(listNewOperations);
		listNewOperations.clear();
		
	//	closeAll(listOperations);
		listOperations.clear();
		
		servSource = null;
		strProjectName = "";
	}

	/**
	 * replace a ci in the workspace with a new revision. Usage in the update system
	 * @param ci
	 * @throws WorkspaceException 
	 */
	public void replaceCI(ConfigurationItem ci) throws WorkspaceException {
		ConfigurationItem ciOld = null;
		for(CIWorkspace ciIt: listICContent)
		{
			if(ciIt.getURI().getPath().equals(ci.getUri().getPath()))
			{
				ciOld = new ConfigurationItem(ciIt.getURI(), null, RevisionID.ZERO_REVISION);
				break;
			}
		}
		
		if(ciOld == null)
		{
			throw new WorkspaceException("Cannot replace a CI item: " + ci.toString(), null);
		}
		
		//faz a substituição
		File fOld = new File(this.getPath(), ciOld.getUri().getPath().toString());
		if(!fOld.exists())
			throw new WorkspaceException("File replaced do not exits: " + ci.toString(), null);
		
		try {
			OutputStream fOut = new FileOutputStream(fOld);
			UtilStream.copy(ci.getItemAsInputStream(), fOut);
			fOut.close();
		} catch (FileNotFoundException e) {
			throw new WorkspaceException("File replaced not found: " + fOld.toString(), null);
		} catch (IOException e) {
			throw new WorkspaceException("File problem at io process: " + fOld.toString(), null);
		}
	}

	public void addNewCI(ConfigurationItem ci) throws WorkspaceException 
	{
		File f = new File(getPath(), ci.getUri().getPath());
		if(f.exists())
			throw new WorkspaceException("File exists: " + f.toString(), null);
		
		FileOutputStream fOut;
		try {
			fOut = new FileOutputStream(f);
			UtilStream.copy(ci.getItemAsInputStream(), fOut);
		} catch (FileNotFoundException e) {
			throw new WorkspaceException("File not found: " + f.toString(), e);
		} catch (IOException e) {
			throw new WorkspaceException("IOException " + f.toString(), e);
		}
		
		try {
			if(fOut != null)
				fOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Collection<File> c = new ArrayList<File>(1);
		c.add(f);		
		addFiles(c);		
	}

	public void getUnmodifiedWorkspace(List<ConfigurationItem> list) throws WorkspaceException 
	{
		try {
			parser.getUnmodifiedFiles(list);
		} catch (WorkspaceConfigParserException e) {
			throw new WorkspaceException("Problem in generate unmodified files",e);
		}		
	}
}

