/**
 * 
 */
package br.uff.ic.gardener.workspace;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import br.uff.ic.gardener.ConfigurationItem;
import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.util.FileHelper;
import br.uff.ic.gardener.util.UtilStream;
import br.uff.ic.gardener.workspace.WorkspaceOperation.Operation;

/**
 * @author Marcos Côrtes
 * 
 */
public class WorkspaceTest{


	static File pathWS = null;

	static Workspace workspace = null;

	@BeforeClass
	public static void setUpClass() throws WorkspaceException, IOException
	{
		pathWS = FileHelper.createTemporaryRandomPath();
		workspace = new Workspace(pathWS);
		createWorkspaceStruct(pathWS, true);
	}
	
	@AfterClass
	public static void tearDownClass()
	{
		workspace.close();
		FileHelper.deleteDirTree(pathWS);
	}
	
	/**
	 * @throws IOException 
	 * @throws WorkspaceException 
	 * @throws java.lang.Exception
	 */

	@Before
	public void setUp() throws IOException, WorkspaceException  {
	}
	
	static Random random = new Random();
	static char INIT_CHAR = 'a';
	static char END_CHAR = 'z';
	
	
	private static String nextString(String str)
	{
		if( str == null || str.length() == 0)
			return new String( new char[] {INIT_CHAR});
		
		char last = str.charAt(str.length()-1);
		if(last < END_CHAR)
		{
			last++;
			if(str.length() == 1)
				str = new String(new char[]{last});
			else
				str = str.substring(0, str.length()-2) + last;
		}else
		{
			str = str + INIT_CHAR;
		}
		
		return str;
	}
	
	private static void fillProfile(PrintStream os, File path, String strPrevPath)
	{
		if(strPrevPath == null || strPrevPath.equals(""))
			strPrevPath = "/";
		
		File[] files = path.listFiles();
		for(File f: files)
		{
			if(f.isDirectory())
			{
				fillProfile(os, f, strPrevPath + "/" + f.getName());
			}
			else if(f.isFile())
			{
				if(
						f.getName().equals(WorkspaceConfigParser.STR_FILE_OPERATION) ||
						f.getName().equals(WorkspaceConfigParser.STR_FILE_PROFILE)
				)
				{
					//faznada
				}else
				{
					if(strPrevPath == null || strPrevPath.equals("/") || strPrevPath.equals(""))
					{
						os.printf("%s s%s", 
							WorkspaceConfigParser.STR_HAS_IC,
							f.getName(),
							UtilStream.getLineSeperator());
					}else
					{
						os.printf("%s %s%s%s%s", 
							WorkspaceConfigParser.STR_HAS_IC, 
							strPrevPath,
							"/",
							f.getName(),
							UtilStream.getLineSeperator());
					}
				}
			}
		}
	}
	
	private static void createWorkspaceStruct(File filePath, boolean fillProfile) throws IOException
	{
		
		createWorkspaceStructDirAndFiles(filePath, 3, 5, 5, false);
		File fileProfile 	= new File(filePath, WorkspaceConfigParser.STR_FILE_PROFILE);
		File fileOperation 	= new File(filePath, WorkspaceConfigParser.STR_FILE_OPERATION);
		
		//o arquivo de operações está vazio
		fileOperation.createNewFile();
		OutputStream os = new FileOutputStream(fileProfile);
		PrintStream ps = new PrintStream(os);
		
		UtilStream.fillPrintStream(ps, 
				WorkspaceConfigParser.STR_SERV_ORIGIN + " null",
				WorkspaceConfigParser.STR_REVISION + " " + RevisionID.ZERO_REVISION.toString(),
				String.format("%s %d", WorkspaceConfigParser.STR_LAST_TIMESTAMP_CHECKOUT, (new Date()).getTime())			
				);
		
		if(fillProfile)
			fillProfile(ps, filePath, "/");
		
		ps.flush();
		os.close();
	}
	
	/*
	 * Cria a estrutura do workspace de arquivos e diretórios
	 */
	public static void createWorkspaceStructDirAndFiles(File filePath, int treeDepth, int fileCount, int dirCount, boolean randomizeFileDirCount)
	throws IOException
	{
		if(!filePath.isDirectory())
			throw new IOException("Arquivo especificado não é uma pasta");
		if(randomizeFileDirCount)
		{
			fileCount 	= 1+random.nextInt(fileCount-1);
			dirCount 	= 1+random.nextInt(dirCount-1);
		}
		
		String nameFile = nextString(null);
		for(int i = 0; i < fileCount; i++)
		{
			//cria os arquivos
			File newFile = new File(filePath, nameFile);
			if(newFile.createNewFile())
			{
				UtilStream.fillLineNumber(new FileOutputStream(newFile), 10);
				UtilStream.fillLineNumber(new FileOutputStream(newFile), 10);
			}
			nameFile = nextString(nameFile);
		}
		
		String nameDir = "p";
		
		for(int i = 0; i <dirCount; i++)
		{
			String nameDirInt = nameDir + Integer.toString(i);
			File newDir = new File(filePath, nameDirInt);
			if(newDir.mkdir())
			{
				if(treeDepth > 0)
					createWorkspaceStructDirAndFiles(newDir, treeDepth-1, fileCount, dirCount, randomizeFileDirCount);
			}
		}
		
	}

	//public final static void createWorkspace() throws WorkspaceException {
	//	// folder
	//	workspace = new Workspace(pathWS);
	//}

	/**
	 * Test method for {@link br.uff.ic.gardener.workspace.Workspace#commit()}.
	 * @throws WorkspaceException 
	 */
	@Test
	public final void testCommit() throws WorkspaceException {
		workspace.commit();
		//workspace.close();
	}


	/**
	 * Test method for {@link br.uff.ic.gardener.workspace.Workspace#checkout()}
	 * .
	 * @throws WorkspaceException 
	 */
	@Test
	public final void testCheckout() throws WorkspaceException 
	{
		Collection<ConfigurationItem> list = new LinkedList<ConfigurationItem>();
		workspace.checkout(RevisionID.LAST_REVISION, list);
	}
	
	@Test
	public final void testAdd() throws WorkspaceException, IOException
	{
		File f1 = new File(pathWS, "abobora1");
		File f2 = new File(pathWS, "abobora2");
		File f3 = new File(pathWS, "abobora3");
		@SuppressWarnings("unused")
		File f4 = new File(pathWS, "abobora4");
		UtilStream.fillFile(f1, "1", "1", "1", "1", "1", "1", "1");
		UtilStream.fillFile(f2, "2", "2", "2", "2", "2", "2", "2");
		UtilStream.fillFile(f3, "3", "3", "3", "3", "3", "3", "3");
		
		LinkedList<File> listFiles = new LinkedList<File>();
		
		FileHelper.findFiles(pathWS, listFiles, "**", true);//tenta adicionar todos de propósito
		
		
		workspace.addFiles(listFiles);
		
		//só deveria adicionar os que não existem na lista
		for(WorkspaceOperation op: workspace.getNewOperationList())
		{
			File file = new File(workspace.getPath(), op.getParamAt(0));
			org.junit.Assert.assertTrue(file.exists());				
		}
	}
	
	@Test
	public final void testRemove() throws IOException, WorkspaceException
	{
	
		LinkedList<File> listFiles = new LinkedList<File>();
		
		FileHelper.findFiles(pathWS, listFiles, "**", true);
		
		workspace.removeFiles(listFiles);
		
		for(WorkspaceOperation op: workspace.getNewOperationList())
		{
			if(op.getOperation() == Operation.REMOVE_FILE)
			{
				File file = new File(workspace.getPath(), op.getParamAt(0));
				org.junit.Assert.assertTrue(!file.exists());
			}
		}
	}
}
