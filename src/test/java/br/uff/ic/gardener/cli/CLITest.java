/**
 * 
 */
package br.uff.ic.gardener.cli;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import br.uff.ic.gardener.comm.ComClient;
import br.uff.ic.gardener.comm.localfake.LocalFakeComClient;
import br.uff.ic.gardener.util.FileHelper;
import br.uff.ic.gardener.util.UtilStream;
import br.uff.ic.gardener.workspace.WorkspaceTest;

/**
 * @author Marcos
 * 
 */
public class CLITest {

	/**
	 * Test method for {@link br.uff.ic.gardener.cli.CLI#me()}.
	 */
	@Test
	public void testMe() {
		if (CLI.me() == null)
			fail("Cannot get CLI singletons");
	}
	

	@Test
	public void testGetActualPath() {
		try {
			File file = CLI.getActualPath();

			if (!file.isDirectory())
				fail("does not generate a valid directory");
		} catch (Exception e) {
			fail("generate exception");
		}
	}
	
	private static File pathServ = null;
	
	private static File pathWS	= null;
	
	@BeforeClass
	public static void setUpClass()
	{
		try
		{
			pathServ = FileHelper.createTemporaryRandomPath();
			pathWS   = FileHelper.createTemporaryRandomPath();
			
			WorkspaceTest.createWorkspaceStructDirAndFiles(pathWS, 2, 2, 2, false);
			
			String tempStrPath = pathServ.getPath();
			tempStrPath = tempStrPath.replace(File.separatorChar, '/');
			String strServ = String.format("filefake:///%s", tempStrPath);
			//CLI.doMain(String.format("init -w\"%s\" -s\"%s\" aboboras", pathWS.toString(), strServ));
			CLI.doMain(new String[]{"init", "-w", pathWS.toString(), "-s", strServ, "abobora"});
			
		}catch(Exception e)
		{
			pathServ = null;
			pathWS = null;
			assertTrue(false);
		}
	}

	@AfterClass
	public static void tearDownClass()
	{
		try
		{
			if(pathWS != null)
				FileHelper.deleteDirTree(pathWS);
			if(pathServ != null)
				FileHelper.deleteDirTree(pathServ);
		}catch(Exception e)
		{
		}
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}
	
	@Test
	public void testDoCommit()
	{
		//String temp = String.format("filefake:///%s", pathServ.getPath());
		CLI.doMain("add -w\"%s\" **", pathWS.toString());
		CLI.doMain("ci -w\"%s\" -m\"Testando commit pós add\"", pathWS.toString());
	}
	
	@Test
	public void testDoDiff()
	{
		File pathDiff = null;
		try
		{
			pathDiff = FileHelper.createFile(CLITest.pathWS, "cliDiff");
		}catch(IOException e)
		{
			fail("FileHelper fail: File pathDiff = FileHelper.createFile(CLITest.pathWS, \"cliDiff\");");
		}
		
		try
		{
			File fileA = new File(pathDiff, "a.txt");
			File fileB = new File(pathDiff, "b.txt");
			UtilStream.fillFile(fileA, "1", "2", "3");
			UtilStream.fillFile(fileB, "1", "2", "4");
			CLI.doMain(String.format("diff %s %s", fileA.toString(), fileB.toString()));
		}catch(Exception e)
		{
			fail("Diff does not execute correctely");
		}
		try
		{
			
			//FileHelper.deleteDirTree(pathDiff);
			FileHelper.deleteDirTree(pathDiff);
		}catch(SecurityException e)
		{
			fail("FileHelper fail: File pathDiff = FileHelper.deleteDirTree(pathDiff);");
		}
	}
}
