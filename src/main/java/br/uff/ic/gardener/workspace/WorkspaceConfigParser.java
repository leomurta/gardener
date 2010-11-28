package br.uff.ic.gardener.workspace;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.LinkedList;

import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.util.FileHelper;
import br.uff.ic.gardener.util.TokenizerWithQuote;
import br.uff.ic.gardener.util.NotFilenameFilter;
import br.uff.ic.gardener.util.UtilStream;


/**
 * Faz o parser do arquivo de workspace
 * @author Marcos
 *
 */
public class WorkspaceConfigParser 
{
	public static final String STR_SERV_ORIGIN = "SERV_ORIGIN";

	public static final String STR_REVISION = "REVISION";

	public static final String STR_LAST_TIMESTAMP_CHECKOUT 	= "LAST_TIMESTAMP_CHECKOUT";
	public static final String STR_HAS_IC					= "HAS_IC";
	
	public static String STR_FILE_PROFILE 		= ".profile";
	
	public static String STR_FILE_OPERATION	= ".operation";

	/*
	 * Instância do workspace a ser alterada pelo arquivo
	 */
	private Workspace workspace = null;

	private File directory = null;
	
	WorkspaceConfigParser(Workspace work, File dir) throws WorkspaceConfigParserException
	{
		directory = dir;
		workspace = work;
		loadConfig();
	}
	
	/**
	 * Load config of a path workspace. Before it clear the Workspace instance
	 * @throws WorkspaceConfigParserException 
	 */
	private void loadConfig() throws WorkspaceConfigParserException
	{
		getWorkspace().reset();
		if(!isWorkspaceDir(this.directory))
			return;
			
		try {
			loadProfile();
		} catch (WorkspaceConfigParserException e) {
			throw new WorkspaceConfigParserException("Não foi possível carregar as configurações do workspace", "", e); 
		}
		
		try {
			loadOperations();
		} catch (WorkspaceConfigParserException e) {
			throw new WorkspaceConfigParserException("Não foi possível carregar as configurações do workspace", "", e);
		}
	}
	
	/**
	 * Carrega o arquivo de configurações
	 * @param listICContent O arquivo de configurações possui uma lista de ICs contidos na revisão corrente, então ele as carrega nesta coleção
	 * @throws WorkspaceConfigParserException Caso haja algum problema de parser ele carrrega aqui
	 */
	private void loadProfile() throws WorkspaceConfigParserException
	{
		
		LinkedList<CIWorkspace> content = new LinkedList<CIWorkspace>();
		
		/**
		 * Guarda o profile do workspace
		 */
		try {
			InputStream inputStreamProfile = new FileInputStream(new File(directory, STR_FILE_PROFILE));
			
			/**
			 * Guarda o arquivo de operation do workspace
			 */
			//InputStream inputStreamOperation = null;
			TokenizerWithQuote twq = new TokenizerWithQuote(inputStreamProfile);
			while(twq.hasMoreTokens())
			{
				String s = twq.nextToken();
				if(s.equals(STR_REVISION))
				{
					String next = twq.nextToken();
					if("null".equals(next))
					{
						next = "0";
					}
					
					getWorkspace().setCurrentRevision(RevisionID.fromString(next));
				}else if(s.equals(STR_LAST_TIMESTAMP_CHECKOUT))
				{
					String next = twq.nextToken();
					if("null".equals(next))
					{
						next = "0";
					}
					try {
						
						long miliS = Long.parseLong(next);
						Date date = new Date(miliS);
						getWorkspace().setCheckoutTime(date);
					} catch (NumberFormatException e) {
						throw new WorkspaceConfigParserException(
								String.format("Não foi possível interpretar %s com valor %s", s,next),
								s, e);
					}
					
				}else if(s.equals(STR_SERV_ORIGIN))
				{
					String next = twq.nextToken();
					try {
						if("null".equals(next))
						{
							getWorkspace().setServSource(null);
						}else
						{
							getWorkspace().setServSource(new URI(next));
						}
					} catch (URISyntaxException e) {
						throw new WorkspaceConfigParserException(
								String.format("Não foi possível interpretar %s com valor %s", s,next),
								s, e);
					}
				}else if(s.equals(STR_HAS_IC))
				{
					String next = twq.nextToken();
					try {
						content.add(new CIWorkspace(new URI(next)));
					} catch (URISyntaxException e) 
					{
						throw new WorkspaceConfigParserException(
							String.format("Não foi possível interpretar %s com valor %s", s,next),
							s, e);
					}
				}
					
			}
			
			inputStreamProfile.close();
			
			getWorkspace().addContent(content);
		} catch (FileNotFoundException e) {
			throw new WorkspaceConfigParserException(
					String.format("Não foi possível interpretar o arquivo %s%s%s",directory.toString(), File.pathSeparator, STR_FILE_PROFILE ),
					"", e);
		}catch(IOException e)
		{
			throw new WorkspaceConfigParserException(
					String.format("Não foi possível interpretar o arquivo %s%s%s",directory.toString(), File.pathSeparator, STR_FILE_PROFILE ),
					"", e);
		}
	}
	
	/**
	 * Load the operations in the operation file. This ignore the new operations realize in this workspace 
	 * @param list the list that receive operations
	 * @throws WorkspaceConfigParserException
	 */
	public void loadOperations() throws WorkspaceConfigParserException
	{
		LinkedList<CIWorkspaceStatus> list = new LinkedList<CIWorkspaceStatus>();
		InputStream inputStreamProfile;
		String token = "";
		try {
			inputStreamProfile = new FileInputStream(new File(directory, STR_FILE_OPERATION));
			TokenizerWithQuote twq = new TokenizerWithQuote(inputStreamProfile);
			while(twq.hasMoreTokens())
			{
				try
				{
				token = twq.nextToken();
				String strFile = twq.nextToken();
				String strFileOld = twq.nextToken();
				if(Status.ADD.isLabel(token))
				{
					list.add(
							new CIWorkspaceStatus(
									new URI(strFile), 
									Status.ADD,
									workspace.getCheckoutTime(),
									null));					
				}
				else if(Status.REM.isLabel(token))
				{
					list.add(
							new CIWorkspaceStatus(
									new URI(strFile), 
									Status.REM,
									workspace.getCheckoutTime(),
									null
									)
							);
				}else if(Status.RENAME.isLabel(token))
				{
					
						list.add(
								new CIWorkspaceStatus(
										new URI(strFile), 
										Status.ADD,
										workspace.getCheckoutTime(),
										new URI(strFileOld)
										)
								);
					
				}else
				{
					throw new WorkspaceConfigParserException("token is not a valid status", token, null);
				}
				} catch (URISyntaxException e) {
					throw new WorkspaceConfigParserException("Cannot interpret token", token, e);
				}
			}
			twq.close();
			getWorkspace().addOldOperation(list);
			
		} catch (FileNotFoundException e) {
			throw new WorkspaceConfigParserException("Cannot load operations", token, e);
		}
		
	}
	private InputStream generateInputStream(String strFile) {
		File file = new File(directory, strFile);
		try
		{
			return new FileInputStream(file);
		}catch(FileNotFoundException e)
		{
			return null;
		}
	}

	/**
	 * Append operations in the operation file
	 * @param list the list of operations to append in the file
	 * @throws WorkspaceConfigParserException
	 */
	private void appendOperations() throws WorkspaceConfigParserException
	{
		OutputStream outputStream;
		String token = "";
		try {
			outputStream = new FileOutputStream(new File(directory, STR_FILE_OPERATION), true);
		
			PrintStream ps = new PrintStream(outputStream, true);
			
			for(CIWorkspace wo: getWorkspace().getNewOperations())
			{
				ps.println(wo.toString());
			}
			ps.close();
		} catch (FileNotFoundException e) {
			throw new WorkspaceConfigParserException("Não foi possível adicionar operações ao arquivo de operações", token, e);
		}
	}

	/**
	 * Save the profile archive.
	 * This is do to save workspace configuration
	 * @throws WorkspaceConfigParserException
	 */
	private void saveProfile()throws WorkspaceConfigParserException
	{
		/**
		 * Guarda o profile do workspace
		 */
		try {
			OutputStream outputStream = new FileOutputStream(new File(directory, STR_FILE_PROFILE));
			PrintStream ps = new PrintStream(outputStream, true);
			
			ps.printf("%s %s%s", STR_SERV_ORIGIN, getWorkspace().getServSource()!=null?workspace.getServSource().toString():"null", UtilStream.getLineSeperator());
			ps.printf("%s %s%s", STR_REVISION, getWorkspace().getCurrentRevision().toString(), UtilStream.getLineSeperator());
			ps.printf("%s %d%s", STR_LAST_TIMESTAMP_CHECKOUT, getWorkspace().getCheckoutTime().getTime(), UtilStream.getLineSeperator());
			
			for(CIWorkspace ci: getWorkspace().getWorkspaceVersionedContent())
			{
				ps.printf("%s \"%s\"%s", STR_HAS_IC, ci.getURI(), UtilStream.getLineSeperator());
			}
			
			ps.close();
		}catch(IOException e)
		{
			throw new WorkspaceConfigParserException(
					String.format("Não foi possível salvar o arquivo %s%s%s",directory.toString(), File.pathSeparator, STR_FILE_PROFILE ),
					"", e);
		}
	}
	
	/**
	 * Save content of father workspace in the directory 
	 */
	public void save() throws WorkspaceConfigParserException
	{
		saveProfile();
		appendOperations();
	}

	/*public void saveWithOutOperation(Collection<CIWorkspace> originalCIContent) throws WorkspaceConfigParserException {
		saveProfile(originalCIContent);

		OutputStream outputStream;
		//String token = "";
		try {
			outputStream = new FileOutputStream(new File(directory, STR_FILE_OPERATION));
			outputStream.close();//faz arquivo vazio
		
		} catch (FileNotFoundException e) {
			throw new WorkspaceConfigParserException("Não foi possível salvar operações vazio", null, e);
		} catch (IOException e) {
			throw new WorkspaceConfigParserException("Não foi possível salvar operações vazio", null, e);
		}
		
	}
	*/


	/**
	 * Return if the path is a configuration file (profile or operation)
	 * @param path
	 * @param f
	 * @return
	 */
	public static boolean isConfigFile(File path, File f)
	{
		return STR_FILE_OPERATION.equals(f.getName()) || STR_FILE_PROFILE.equals(f.getName());
	}

	/**
	 * Verify if the path contain the files for workspace configuration.
	 * @param path
	 * @return
	 */
	public boolean isWorkspaceDir(File path) {
		File fProfile 	= new File(directory, STR_FILE_PROFILE);
		File fOp 		= new File(directory, STR_FILE_OPERATION);
		return fProfile.exists() && fOp.exists();
	}
	

	static public FileFilter getNotFileConfigFilter()
	{
		return new NotFilenameFilter(STR_FILE_PROFILE, STR_FILE_OPERATION); 
	}
	
/*
	public void loadRealICContent(Collection<CIWorkspaceStatus> collReal) throws WorkspaceConfigParserException {
		Queue<File> list = new LinkedList<File>();
		list.add(this.directory);
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
						collReal.add(
								new CIWorkspaceStatus(
										FileHelper.getRelative(directory, f),
										new FileInputStream(f),
										Status.UNVER,
										new Date(f.lastModified()),
										null
									)
								);
					} catch (FileNotFoundException e) {
						throw new WorkspaceConfigParserException("Can not open a file", f.toString(), e);
					}
				}
			}
		}
	}
	*/
	/*
	public void loadICToCommit(Collection<ConfigurationItem> collDest) throws WorkspaceConfigParserException
	{
		Queue<File> list = new LinkedList<File>();
		list.add(this.directory);
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
										FileHelper.getRelative(directory, f),
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
	}*/

	/*
	public void checkout(RevisionID revision, Collection<ConfigurationItem> list) throws WorkspaceConfigParserException 
	{
		LinkedList<CIWorkspace> listOut =new LinkedList<CIWorkspace>();
		// erase content
		File[] listContent = this.directory.listFiles();
		for(File f: listContent)
		{
			FileHelper.deleteDirTree(f);
		}

		//save content
		for (ConfigurationItem e : list) {
			File f = null;
			try {
				f = FileHelper.createFile(this.directory, e.getStringID());
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
			
			try {
				CIWorkspace ci = new CIWorkspace(e.getUri(), new FileInputStream(f), new Date());
				listOut.add(ci);
			} catch (FileNotFoundException e1) {
				throw new WorkspaceConfigParserException("Cannot open file", f.toString(), e1);
			}
		}

		getWorkspace().addContent(listOut);
		this.save();		
		
	}*/



	private Workspace getWorkspace() {
		return workspace;
		
	}

	public boolean isConfigFile(File f) {
		return isConfigFile(directory, f);
	}

	/*
	public void rename(File fileSource, String strNewName) throws WorkspaceConfigParserException {
		
	}
	*/
	
	public URI getRelativeURI(URI item)
	{
		return FileHelper.getRelative(directory.toURI(), item);
	}

	public File getPath() {
		return directory;
	}

	public void clearOperationData() throws WorkspaceConfigParserException {
		try {
			OutputStream outputStream = new FileOutputStream(new File(directory, STR_FILE_OPERATION));
			outputStream.close();//faz arquivo vazio
		
		} catch (FileNotFoundException e) {
			throw new WorkspaceConfigParserException("Não foi possível salvar operações vazio", null, e);
		} catch (IOException e) {
			throw new WorkspaceConfigParserException("Não foi possível salvar operações vazio", null, e);
		}
	}

}
