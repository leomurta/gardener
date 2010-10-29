/**
 * 
 */
package br.uff.ic.gardener.workspace;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.mongodb.io.StreamUtil;

import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.client.StubAPIClient;
import br.uff.ic.gardener.util.TestWithTemporaryPath;
import br.uff.ic.gardener.util.UtilStream;

/**
 * @author Marcos Côrtes
 * 
 */
public class WorkspaceTest extends TestWithTemporaryPath{


	File pathWS = null;

	Workspace workspace = null;

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
	private static void createWorkspaceStructDirAndFiles(File filePath, int treeDepth, int fileCount, int dirCount, boolean randomizeFileDirCount)
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
				UtilStream.fillFile(newFile, nameFile + "1", nameFile + "2" , nameFile + "3", nameFile + "4"  );
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

	public final void createWorkspace() throws WorkspaceException {
		// folder
		workspace = new Workspace(pathWS, new StubAPIClient());
	}

	/**
	 * Test method for {@link br.uff.ic.gardener.workspace.Workspace#commit()}.
	 * @throws WorkspaceException 
	 */
	@Test
	public final void testCommit() throws WorkspaceException {
		createWorkspace();
		workspace.commit();
		workspace.close();
	}

	private static void fillFile(File file, String... strVec) {
		FileWriter fw;
		try {
			fw = new FileWriter(file);
			PrintWriter pw = new PrintWriter(fw);

			for (String str : strVec) {
				pw.append(str + "\n");
			}
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link br.uff.ic.gardener.workspace.Workspace#checkout()}
	 * .
	 * @throws WorkspaceException 
	 */
	@Test
	public final void testCheckout() throws WorkspaceException {
		createWorkspace();
		workspace.checkout(RevisionID.LAST_REVISION);
		workspace.close();
	}
	
	@Test
	public final void testAdd() throws WorkspaceException, IOException
	{
		createWorkspaceStruct(getPath(),false);
		workspace = new Workspace(getPath(), new StubAPIClient());
		
		LinkedList<File> listFiles = new LinkedList<File>();
		
		UtilStream.findFiles(getPath(), listFiles, "**", true);
		
		
		workspace.addFiles(listFiles);
		
		for(WorkspaceOperation op: workspace.getNewOperationList())
		{
			File file = new File(workspace.getPath(), op.getParamAt(0));
			org.junit.Assert.assertTrue(file.exists());				
		}
	}
	
	@Test
	public final void testRemove() throws IOException, WorkspaceException
	{
		createWorkspaceStruct(getPath(), true);
		workspace = new Workspace(getPath(), new StubAPIClient());
		
		LinkedList<File> listFiles = new LinkedList<File>();
		
		UtilStream.findFiles(getPath(), listFiles, "**", true);
		
		workspace.removeFiles(listFiles);
		
		for(WorkspaceOperation op: workspace.getNewOperationList())
		{
			File file = new File(workspace.getPath(), op.getParamAt(0));
			org.junit.Assert.assertTrue(!file.exists());				
		}
	}
}
