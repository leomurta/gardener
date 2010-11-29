/**
 * 
 */
package br.uff.ic.gardener.workspace;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import br.uff.ic.gardener.ConfigurationItem;
import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.util.FileHelper;
import br.uff.ic.gardener.util.UtilStream;

/**
 * @author Marcos Côrtes
 * 
 */
public class WorkspaceTest{


	File pathWS = null;

	Workspace workspace = null; 

	@Before
	public void setUp() throws WorkspaceException, IOException
	{
		pathWS = FileHelper.createTemporaryRandomPath();
		createWorkspaceStructDirAndFiles(pathWS, 2, 2, 2, false);
		workspace = new Workspace(pathWS);
		//createWorkspaceStruct(pathWS, false);
		workspace.saveConfig();
	}
	
	@After
	public void tearDown() throws WorkspaceException
	{
		try {
			workspace.close();
		} catch (IOException e) {
			fail(e.getStackTrace().toString());
		}
		FileHelper.deleteDirTree(pathWS);
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
		
		createWorkspaceStructDirAndFiles(filePath, 2, 2, 2, false);
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
		ps.close();
	}
	
	/*
	 * Cria a estrutura do workspace de arquivos e diretórios
	 */
	public static void createWorkspaceStructDirAndFiles(File filePath, int treeDepth, int _fileCount, int _dirCount, boolean randomizeFileDirCount)
	throws IOException
	{
		if(!filePath.isDirectory())
			throw new IOException("Arquivo especificado não é uma pasta");
		
		int fileCount = _fileCount;
		int dirCount  = _dirCount;
		
		if(randomizeFileDirCount)
		{
			fileCount 	= 1+random.nextInt(_fileCount-1);
			dirCount 	= 1+random.nextInt(_dirCount-1);
		}
		
		String nameFile = nextString(null);
		for(int i = 0; i < fileCount; i++)
		{
			//cria os arquivos
			File newFile = new File(filePath, nameFile);
			if(newFile.createNewFile())
			{
				OutputStream out = new FileOutputStream(newFile);
				UtilStream.fillLineNumber(out, 10);
				out.close();
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
					createWorkspaceStructDirAndFiles(newDir, treeDepth-1, _fileCount, _dirCount, randomizeFileDirCount);
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
		//workspace.commit();
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
		FileHelper.fillFile(f1, "1", "1", "1", "1", "1", "1", "1");
		FileHelper.fillFile(f2, "2", "2", "2", "2", "2", "2", "2");
		FileHelper.fillFile(f3, "3", "3", "3", "3", "3", "3", "3");
		
		LinkedList<File> listFiles = new LinkedList<File>();
		
		FileHelper.findFiles(pathWS, listFiles, "**", true);//tenta adicionar todos de propósito
		
		
		workspace.addFiles(listFiles);
		
		//só deveria adicionar os que não existem na lista
		for(CIWorkspaceStatus op: workspace.getNewOperations())
		{
			File file = new File(pathWS, op.getStringID());
			org.junit.Assert.assertTrue(file.exists());				
		}
	}
	
	@Test
	public final void testRemove() throws IOException, WorkspaceException
	{
	
		LinkedList<File> listFiles = new LinkedList<File>();
		
		FileHelper.findFiles(pathWS, listFiles, "**", true);
		workspace.addFiles(listFiles);
		workspace.setCommited(RevisionID.generateNewRevision(workspace.getCurrentRevision().getNumber()));
		workspace.saveConfig();
//		createWorkspaceStructDirAndFiles(pathWS, 2, 2, 2, false);
		workspace.removeFiles(listFiles);
		
		for(CIWorkspaceStatus op: workspace.getOperations())
		{
			if(op.getStatus() == Status.REM)
			{
				File file = new File(pathWS, op.getStringID());
				org.junit.Assert.assertTrue(!file.exists());
			}
		}
	}
	
	@Test
	public void testStatus()
	{
		
		Collection<CIWorkspaceStatus> coll = new LinkedList<CIWorkspaceStatus>();
		workspace.clearOperations();
		//primeiro vê se exibe como status todos os arquivos unversioned
		try {
			workspace.getStatus(coll);
		} catch (WorkspaceException e) {
			e.printStackTrace();
			fail("Cannot getStatus in workspace");
		}
		
		for(CIWorkspaceStatus ciw: coll)
		{
			assertTrue(ciw.getStatus()==Status.UNVER);
			assertTrue((new File(pathWS, ciw.getStringID()).exists()));
		}
		
		//======================================================
		//adiciona todos os caras com nome "a"		
		//======================================================
		Collection<File> list = new LinkedList<File>();
		list = FileHelper.findFiles(pathWS, list, "a", true);
		try {
			workspace.addFiles(list);
			workspace.saveConfig();
		} catch (WorkspaceException e) {
			fail("Erro no add");
		}
		
		try
		{
			coll.clear();
			workspace.getStatus(coll);
			
			//verfica
			for(CIWorkspaceStatus ciw: coll)
			{
				if(ciw.getStatus()==Status.ADD)
				{
					final String name = ciw.getStringID();
					assertTrue(name.charAt(name.length()-1) == 'a');
					File f = new File(pathWS, name);
					assertTrue(f.exists() && f.isFile());
				}
			}
			
		} catch (WorkspaceException e) {
			e.printStackTrace();
			fail("Cannot getStatus in workspace pós add");
		}
		
		//======================================================
		//faz remove "b"
		//======================================================
		list.clear();
		list = new LinkedList<File>();
		list = FileHelper.findFiles(pathWS, list, "b", true);
		
		//tenta remover itens não adicionados, deveria dar exceção
		for(File f: list)
		{
			try
			{
				workspace.removeFile(f);
				fail("Deveria gerar uma exceção por tentar inserir um arquivo não versionado");
			} catch (WorkspaceException e) {
			}
		}
		
		try {
			workspace.addFiles(list);
			workspace.setCommited(RevisionID.generateNewRevision(workspace.getCurrentRevision().getNumber()));//força o commit
			workspace.saveConfig();
		} catch (WorkspaceException e) {
			fail("Erro no add2");
		}
		
		//remove o adicionado e commitado
		try {
			workspace.removeFiles(list);
			workspace.saveConfig();
		} catch (WorkspaceException e) {
			fail("Erro no remove");
		}
		
		try
		{
			coll.clear();
			workspace.getStatus(coll);
			
			//verfica
			for(CIWorkspaceStatus ciw: coll)
			{
				final String name = ciw.getStringID();
			/*	if(ciw.getStatus()==Status.ADD)
				{
					assertTrue(name.charAt(name.length()-1) == 'a');
					File f = new File(pathWS, name);
					assertTrue(f.exists() && f.isFile());
				}else*/ if(ciw.getStatus()==Status.REM)
				{
					assertTrue(name.charAt(name.length()-1) == 'b');
					File f = new File(pathWS, name);
					assertTrue(!f.exists());
				}else
				{
					if(ciw.getStatus() == Status.UNVER)
					{
						int i = 0; 
						i ++;
					}
					assertTrue(ciw.getStatus()==Status.VER);
					char lastChar = name.charAt(name.length()-1);
					assertTrue(lastChar == 'a');
					File f = new File(pathWS, name);
					assertTrue(f.exists());
				}
			}
			
		} catch (WorkspaceException e) {
			e.printStackTrace();
			fail("Cannot getStatus in workspace pós add");
		}
		
		//======================================================
		//faz add do resto
		//======================================================
		list.clear();
		list = new LinkedList<File>();
		list = FileHelper.findFiles(pathWS, list, "**", true);
		try {
			workspace.addFiles(list, true);
			workspace.saveConfig();
		} catch (WorkspaceException e) {
			fail("Erro no add resto");
		}
		
		try
		{
			coll.clear();
			workspace.getStatus(coll);
			//verfica
			for(CIWorkspaceStatus ciw: coll)
			{
				assertTrue(ciw.getStatus()!=Status.UNVER);//< não existem itens não versionados
				final String name = ciw.getStringID();
				if(ciw.getStatus()==Status.ADD)
				{
					assertTrue(name.charAt(name.length()-1) != 'b');
					File f = new File(pathWS, name);
					assertTrue(f.exists() && f.isFile());
				}else if(ciw.getStatus()==Status.REM)
				{
					assertTrue(name.charAt(name.length()-1) == 'b');
					File f = new File(pathWS, name);
					assertTrue(!f.exists());
				}else if(ciw.getStatus() == Status.VER)
				{
					assertTrue(name.charAt(name.length()-1) == 'a');
					File f = new File(pathWS, name);
					assertTrue(f.exists());					
				}else
				{
					fail("Unexpected option");
				}
			/*	if(ciw.getInputStream() != null)
				{
					try {
						ciw.getInputStream().close();
					} catch (IOException e) {
						fail("Cannot close a inputstream: 3");
					}
				}*/
			}
		} catch (WorkspaceException e) {
			e.printStackTrace();
			fail("Cannot getStatus in workspace pós add all");
		}
	}
	
	/**
	 * Testa operações que exigem commit
	 */
	@Test
	public void TestStatus2()
	{
		//fail("Not implemented");
	}
	
}
