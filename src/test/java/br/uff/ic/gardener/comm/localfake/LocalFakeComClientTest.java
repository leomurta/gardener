package br.uff.ic.gardener.comm.localfake;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import br.uff.ic.gardener.ConfigurationItem;
import br.uff.ic.gardener.RevisionCommited;
import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.comm.ComClient;
import br.uff.ic.gardener.comm.ComClientException;
import br.uff.ic.gardener.util.DirectoryFileFilter;
import br.uff.ic.gardener.util.FileHelper;
import br.uff.ic.gardener.util.ORFileFilter;
import br.uff.ic.gardener.util.UtilStream;
import br.uff.ic.gardener.workspace.Workspace;
import br.uff.ic.gardener.workspace.WorkspaceTest;

public class LocalFakeComClientTest {

	File pathWS = null;
	
	File pathServ = null;
	
	static ComClient comClient = null;
	@Before
	public void setUpBefore()
	{
		try
		{
			pathServ = FileHelper.createTemporaryRandomPath();
			pathWS   = FileHelper.createTemporaryRandomPath();
			
			
			//cria serv
			comClient = new LocalFakeComClient(pathServ);
			comClient.init("test");//<cria configuração para versão zero
		}catch(Exception e)
		{
			pathServ = null;
			pathWS = null;
			assertTrue(false);
		}
	}

	
	@After
	public  void tearDownAfter()
	{
		try
		{
			comClient.close();
			FileHelper.deleteDirTree(pathWS);
			FileHelper.deleteDirTree(pathServ);
		}catch(Exception e)
		{
		}
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCheckout() throws ComClientException, LocalFakeComClientException, IOException 
	{	
		WorkspaceTest.createWorkspaceStructDirAndFiles(pathWS, 2, 2, 2, false);
		List<ConfigurationItem> coll = new LinkedList<ConfigurationItem>();
		comClient.checkout("teste", RevisionID.ZERO_REVISION, coll);
	}

	@Test
	public void testCommit() throws IOException
	{
		WorkspaceTest.createWorkspaceStructDirAndFiles(pathWS, 2, 2, 2, false);
		File pathTemp = null;
		try {
			pathTemp = FileHelper.createTemporaryRandomPath();
		} catch (IOException e3) {
			fail("Problem in create pathtemp");
		}
		try
		{
			{
				List<ConfigurationItem> list = new LinkedList<ConfigurationItem>();
			
				fillColl(pathWS, pathWS, list);
			
				comClient.commit("teste", "msg", "", list);
			}
			{
				List<ConfigurationItem> list = new LinkedList<ConfigurationItem>();
				comClient.checkout("teste", RevisionID.LAST_REVISION, list);
				for(ConfigurationItem ic: list)
				{
					File f = FileHelper.createFile(pathTemp, ic.getStringID());
					UtilStream.copy(ic.getItemAsInputStream(), new FileOutputStream(f));
				}
				
				//coloquei para ignorar diretórios pq o diretório vazio do workspace não foi versionado, então no comparar ele iria dar conflito nisso, u diretório vazio no WS não exisitira no checkout do servidor
				boolean ok = FileHelper.comparDirs(pathTemp, pathWS, new ORFileFilter(Workspace.getNotFileConfigFilter(), new DirectoryFileFilter()));
				assertTrue("Checkout data do not equal to the workspace data.", ok);
			}
		}catch(FileNotFoundException e1)
		{
			fail("Problem in create file " + e1.toString());
		}catch(ComClientException e2)
		{			
			fail("Problem in ComCLient : " + e2.toString());
		}
		catch (IllegalArgumentException e3) 
		{
			fail("Problem in Argument : " + e3.toString());
		} catch (IOException e4) {
			fail("IO exception " + e4.toString());
		}
	}
	
	static public void fillColl(File radixPath, File path, Collection<ConfigurationItem> coll) throws FileNotFoundException
	{
		if(path.isDirectory())
		{
			for(File f: path.listFiles())
				fillColl(radixPath, f, coll);
			
		}else if(path.isFile())
		{
			coll.add(new ConfigurationItem(FileHelper.getRelative(radixPath, path), new FileInputStream(path), RevisionID.LAST_REVISION));
		}
		
	}
	
	@Test
	public void generateLogTest()
	{
		{
		List<ConfigurationItem> list = new LinkedList<ConfigurationItem>();
		for(int i = 1; i <= 20; i++)
		{
			File f = new File(pathWS, String.format("%d.txt", i));
			try {
				f.createNewFile();
				list.add(new ConfigurationItem(
						FileHelper.getRelative(pathWS, f),
						new ByteArrayInputStream(new byte[]{'a','b', 'c', ':', (byte) i}),
						new RevisionID(i)
						));
				comClient.commit("", String.format("msg:%d", i), String.format("user:%d", i), list);
			} catch (IOException e) {
				fail("cannot create file " + f.toString());
			} catch (ComClientException e) {
				fail("cannot commClient" + f.toString());
			}
		}
		}
		
		//depois que comitou tem de verificar o log
		List<RevisionCommited> list = new LinkedList<RevisionCommited>();
		try {
			comClient.generateLog(list, new RevisionID(1), new RevisionID(20));
		} catch (ComClientException e) {
			fail("ComClient cannot generateLog");
		}
		
		int i = 1; 
		for(RevisionCommited rc: list)
		{
			assertEquals(String.format("msg:%d",i), rc.getMessage());
			assertEquals(String.format("user:%d",i), rc.getUser());
			assertEquals(new RevisionID(i), rc.getId());			
			i++;
		}
	}

}
