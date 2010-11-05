/**
 * 
 */
package br.uff.ic.gardener.cli;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

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
	
	//private static File pathWS	= null;
	
	@BeforeClass
	public static void setUpClass()
	{
		try
		{
			pathServ = FileHelper.createTemporaryRandomPath();
			File pathWS   = FileHelper.createTemporaryRandomPath();
			
			
			//CLI.doMain(String.format("init -w\"%s\" -s\"%s\" aboboras", pathWS.toString(), strServ));
			CLI.doMain(new String[]{"init", "-w", pathWS.toString(), "-s", getStrServ(), "abobora"});
			
			WorkspaceTest.createWorkspaceStructDirAndFiles(pathWS, 2, 2, 2, false);
			
			CLI.doMain("add -w\"%s\" **", pathWS.toString());
			CLI.doMain("add -ci -w\"%s\" -s\"%s\"", pathWS.toString(), getStrServ());
			FileHelper.deleteDirTree(pathWS);
		}catch(Exception e)
		{
			pathServ = null;
			//pathWS = null;
			assertTrue(false);
		}
	}
	
	private static String getStrServ()
	{
		String tempStrPath = pathServ.getPath();
		tempStrPath = tempStrPath.replace(File.separatorChar, '/');
		return String.format("filefake:///%s", tempStrPath);
	}
	
	@Test
	public void testCheckoutTempWorkspace() throws IOException
	{
		File f = checkoutTempWorkspace();
		FileHelper.deleteDirTree(f);
	}
	
	private File checkoutTempWorkspace() throws IOException
	{
		File pathWorkspace;
			pathWorkspace = FileHelper.createTemporaryRandomPath();
		
		CLI.doMain("co -w\"%s\" -s\"%s\"", pathWorkspace.toString(), getStrServ());
		return pathWorkspace;
	}

	@AfterClass
	public static void tearDownClass()
	{
		try
		{
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
	
	/**
	 * Test a simple Case of checkout.
	 */
	@Test
	public void testGeneral()
	{
		
	}
	
	@Test
	public void testDoCommit() throws IOException
	{
		File pathWS = checkoutTempWorkspace();
		for(int i = 0; i < 30; i++)
		{
			File f = new File(pathWS, UUID.randomUUID().toString());
			try
			{
				f.createNewFile();
				UtilStream.fillRandomData(new FileOutputStream(f), 512);
			}catch(IOException e)
			{
				
			}
		}
		//String temp = String.format("filefake:///%s", pathServ.getPath());
		CLI.doMain("add -w\"%s\" **", pathWS.toString());
		CLI.doMain("ci -w\"%s\" -m\"Testando commit pós add\"", pathWS.toString());
		
		FileHelper.deleteDirTree(pathWS);
	}
	
	
	@Test
	public void testDoDiff()
	{
		try
		{
			File fileA = FileHelper.createTemporaryRandomFile();
			File fileB = FileHelper.createTemporaryRandomFile();
			UtilStream.fillFile(fileA, "1", "2", "3");
			UtilStream.fillFile(fileB, "1", "2", "4");
			CLI.doMain(String.format("diff %s %s", fileA.toString(), fileB.toString()));
			fileA.delete();
			fileB.delete();
		}catch(Exception e)
		{
			fail("Diff does not execute correctely");
		}
	}
}
