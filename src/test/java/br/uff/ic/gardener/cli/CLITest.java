/**
 * 
 */
package br.uff.ic.gardener.cli;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import br.uff.ic.gardener.comm.ComClient;
import br.uff.ic.gardener.comm.localfake.LocalFakeComClient;
import br.uff.ic.gardener.util.FileHelper;
import br.uff.ic.gardener.util.TextHelper;
import br.uff.ic.gardener.util.UtilStream;
import br.uff.ic.gardener.workspace.WorkspaceTest;
import br.uff.ic.gardener.ConfigurationItem;
import br.uff.ic.gardener.RevisionID;

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
			CLI.doMain(new String[]{"init", "-w", pathWS.toString(), "-s", getStrServ(pathServ), "abobora"});
			
			WorkspaceTest.createWorkspaceStructDirAndFiles(pathWS, 2, 2, 2, false);
			
			CLI.doMain("add -w\"%s\" **", pathWS.toString());
			CLI.doMain("add -ci -w\"%s\" -s\"%s\"", pathWS.toString(), getStrServ(pathServ));
			FileHelper.deleteDirTree(pathWS);
		}catch(Exception e)
		{
			pathServ = null;
			//pathWS = null;
			assertTrue(false);
		}
	}
	
	private static String getStrServ(File pathServ2)
	{
		String tempStrPath = pathServ2.getPath();
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
		
		CLI.doMain("co -w\"%s\" -s\"%s\"", pathWorkspace.toString(), getStrServ(pathServ));
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
	
	//@Test
	public void testDoCheckOut() throws IOException, URISyntaxException
	{
		File pathServ = FileHelper.createTemporaryRandomPath();
		createServWithRandomContent(pathServ);
		
		//new workspace
		File pathWorkspace;
		pathWorkspace = FileHelper.createTemporaryRandomPath();		
		for(int i = 0; i < RevisionCount; i++)
		{
			CLI.doMain("co -w\"%s\" -s\"%s\" -r\"%d\"", pathWorkspace.toString(), getStrServ(pathServ), i);
			
			//deleta conteúdo
			File[] childs = pathWorkspace.listFiles();
			for(File child: childs)
				FileHelper.deleteDirTree(child);
		}
		
	}
	
	final static int RevisionCount = 20;
	public static void createServWithRandomContent(File f) throws IOException, URISyntaxException
	{
		
		if(!f.isDirectory())
			return;
		
		File configPath = new File(f, LocalFakeComClient.STR_CONFIG_PATH);
		configPath.mkdir();
		
		ArrayList<ConfigurationItem> arrayCI = new ArrayList<ConfigurationItem>(20); 
		for(int nRevision = 0; nRevision < RevisionCount; nRevision++)//quantas revisões
		{
			arrayCI.clear();
			for(int nFile = 0; nFile < 20; nFile++)//número de arquivos
			{
				String aa = createRandomPathFile(String.format("file_%d_", nFile));
				Random r = new Random();
				byte[] bbb = new byte[30];
				r.nextBytes(bbb);
				InputStream is = new ByteArrayInputStream(bbb);
				ConfigurationItem ci = new ConfigurationItem(new URI("ci:" + aa),is, new RevisionID(nRevision));
				arrayCI.add(ci);
			}
			zipIt(new File(configPath, (new RevisionID(nRevision)).toString() + ".zip") , arrayCI);
		}
		
		File configFile = FileHelper.createFile(configPath, LocalFakeComClient.CONFIG_PROPERTIES);
		Properties properties = new Properties();
		properties.setProperty("LastRevision",
				RevisionID.ZERO_REVISION.toString());
		properties.store(new FileOutputStream(configFile), "");
	}
	
	private static void zipIt(File file, ArrayList<ConfigurationItem> arrayCI) throws IOException 
	{
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file));
		for (ConfigurationItem ci : arrayCI) {
			LocalFakeComClient.zipInputStream(ci.getStringID(),ci.getItemAsInputStream(), zos);
		}
		zos.close();
	}


	private static String createRandomPathFile(String prefix)
	{
		Random r = new Random();
		
		int pathCount = r.nextInt(3);
		StringBuilder pathSB = new StringBuilder();
		pathSB.append('/');
		while(pathCount > 0)
		{
			pathSB.append(TextHelper.randomString(5));
			pathSB.append('/');
			pathCount--;
		}
		
		return String.format("%s%s%s",pathSB.toString(), prefix, TextHelper.randomString(5) );
		
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

	/**
	 * Cenário 1 Cria, checkout, commit
	 * @throws IOException 
	 */
	@Test
	public void Scene1() throws IOException
	{
		//cria o path do servidor fake
		File pathS = FileHelper.createTemporaryRandomPath();
		
		//cria o path do workspace
		File pathWS   = FileHelper.createTemporaryRandomPath();
		
		
		CLI.doMain(new String[]{"init", "-w", pathWS.toString(), "-s", getStrServ(pathServ), "abobora"});
		
		
		//cria arquivos
		File fa = FileHelper.createFile(pathWS, "a.txt"); UtilStream.fillFile(fa, "a", "b", "c", "d");
		File fb = FileHelper.createFile(pathWS, "b.txt"); UtilStream.fillFile(fb, "x", "y", "w", "z");
		File fc = FileHelper.createFile(pathWS, "c.txt"); UtilStream.fillFile(fc, "1", "2", "3", "4");
		
		CLI.doMain("add -w\"%s\" **", pathWS.toString());
		
		CLI.doMain("add -ci -w\"%s\" -s\"%s\"", pathWS.toString(), getStrServ(pathServ));
		
		File pathWS_New = FileHelper.createTemporaryRandomPath();
		
		CLI.doMain("add -co -w\"%s\" -s\"%s\"", pathWS_New.toString(), getStrServ(pathServ));
		
		boolean ok = FileHelper.comparDirs(pathWS, pathWS_New);
		assertTrue(ok);
		FileHelper.deleteDirTree(pathS);
		FileHelper.deleteDirTree(pathWS);
		
	}
}
