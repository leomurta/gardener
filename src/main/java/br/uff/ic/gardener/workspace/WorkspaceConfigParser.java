package br.uff.ic.gardener.workspace;

import java.io.File;
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
import java.util.List;

import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.util.TokenizerWithQuote;
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

	public static final String STR_LAST_TIMESTAMP_CHECKOUT = "LAST_TIMESTAMP_CHECKOUT";
	
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
	
	public void loadProfile() throws WorkspaceConfigParserException
	{
		
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
			while(twq.hasNextToken())
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
	
	public void loadOperations(List<WorkspaceOperation> list) throws WorkspaceConfigParserException
	{
		list.clear();
		InputStream inputStreamProfile;
		String token = "";
		try {
			inputStreamProfile = new FileInputStream(new File(directory, STR_FILE_OPERATION));
			TokenizerWithQuote twq = new TokenizerWithQuote(inputStreamProfile);
			while(twq.hasNextToken())
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
			throw new WorkspaceConfigParserException("Não foi possível carregar as operações", token, e);
		}
		
	}
	
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
}
