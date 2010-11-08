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
import java.util.Collection;
import java.util.Date;
import java.util.List;

import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.util.TokenizerWithQuote;
import br.uff.ic.gardener.util.NotFilenameFilter;
import br.uff.ic.gardener.workspace.WorkspaceOperation;
import br.uff.ic.gardener.workspace.WorkspaceOperation.Operation;


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
	
	WorkspaceConfigParser(Workspace work, File dir)
	{
		directory = dir;
		workspace = work;
	}
	
	/**
	 * Carrega o arquivo de configurações
	 * @param collIC O arquivo de configurações possui uma lista de ICs contidos na revisão corrente, então ele as carrega nesta coleção
	 * @throws WorkspaceConfigParserException Caso haja algum problema de parser ele carrrega aqui
	 */
	public void loadProfile(Collection<URI> collIC) throws WorkspaceConfigParserException
	{
		
		collIC.clear();
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
					
					workspace.setCurrentRevision(RevisionID.fromString(next));
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
						workspace.setCheckoutTime(date);
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
							workspace.setServSource(null);
						}else
						{
							workspace.setServSource(new URI(next));
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
						collIC.add(new URI(next));
					} catch (URISyntaxException e) 
					{
									
						throw new WorkspaceConfigParserException(
							String.format("Não foi possível interpretar %s com valor %s", s,next),
							s, e);
					}
				}
					
			}
			
			inputStreamProfile.close();
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
	public void loadOperations(List<WorkspaceOperation> list) throws WorkspaceConfigParserException
	{
		list.clear();
		InputStream inputStreamProfile;
		String token = "";
		try {
			inputStreamProfile = new FileInputStream(new File(directory, STR_FILE_OPERATION));
			TokenizerWithQuote twq = new TokenizerWithQuote(inputStreamProfile);
			while(twq.hasMoreTokens())
			{
				token = twq.nextToken();
				if(Operation.ADD_FILE.getLabel().equals(token))
				{
					String strFile = twq.nextToken();
					list.add(new WorkspaceOperation(Operation.ADD_FILE, strFile));					
				}
				else if(Operation.REMOVE_FILE.getLabel().equals(token))
				{
					String strFile = twq.nextToken();
					list.add(new WorkspaceOperation(Operation.REMOVE_FILE, strFile));
				}else if(Operation.RENAME_FILE.getLabel().equals(token))
				{
					String strFile1 = twq.nextToken();
					String strFile2 = twq.nextToken();
					list.add(new WorkspaceOperation(Operation.RENAME_FILE, strFile1, strFile2));
				}
			}
			
			twq.close();
			
		} catch (FileNotFoundException e) {
			throw new WorkspaceConfigParserException("Cannot load operations", token, e);
		}
		
	}
	/**
	 * Append operations in the operation file
	 * @param list the list of operations to append in the file
	 * @throws WorkspaceConfigParserException
	 */
	public void appendOperations(List<WorkspaceOperation> list) throws WorkspaceConfigParserException
	{
		OutputStream outputStream;
		String token = "";
		try {
			outputStream = new FileOutputStream(new File(directory, STR_FILE_OPERATION), true);
		
		PrintStream ps = new PrintStream(outputStream, true);
		
		for(WorkspaceOperation wo: list)
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
	public void saveProfile() throws WorkspaceConfigParserException
	{
		/**
		 * Guarda o profile do workspace
		 */
		try {
			OutputStream outputStream = new FileOutputStream(new File(directory, STR_FILE_PROFILE));
			PrintStream ps = new PrintStream(outputStream, true);
			
			ps.printf("%s %s\n", STR_SERV_ORIGIN, workspace.getServSource()!=null?workspace.getServSource().toString():"null");
			ps.printf("%s %s\n", STR_REVISION, workspace.getCurrentRevision().toString());
			ps.printf("%s %d\n", STR_LAST_TIMESTAMP_CHECKOUT, workspace.getCheckoutTime().getTime());
			
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
		appendOperations(this.workspace.getNewOperationList());
	}

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

	public FileFilter getNotFileConfigFilter()
	{
		return new NotFilenameFilter(STR_FILE_PROFILE, STR_FILE_OPERATION); 
	}
}
