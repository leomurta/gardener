package br.uff.ic.gardener.comm.localfake;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import br.uff.ic.gardener.comm.ComClient;
import br.uff.ic.gardener.comm.ComClientException;
import br.uff.ic.gardener.comm.local.LocalComClient;
import br.uff.ic.gardener.comm.local.LocalComClientTest;
import br.uff.ic.gardener.util.FileHelper;
import br.uff.ic.gardener.workspace.WorkspaceTest;

public class LocalFakeComClientTest {

	static
	File pathWS = null;
	
	static
	File pathServ = null;
	
	@BeforeClass
	public static void setUpBeforeClass()
	{
		try
		{
			pathServ = FileHelper.createTemporaryRandomPath();
			pathWS   = FileHelper.createTemporaryRandomPath();
			
			WorkspaceTest.createWorkspaceStructDirAndFiles(pathWS, 2, 2, 2, false);
			
			//cria serv
			ComClient com = new LocalFakeComClient(pathServ);
			com.init("test");//<cria configuração para versão zero
		}catch(Exception e)
		{
			pathServ = null;
			pathWS = null;
			assertTrue(false);
		}
	}

	
	@AfterClass
	public static void tearDownAfterClass()
	{
		try
		{
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
	public void testCheckout() throws FileNotFoundException, ComClientException, LocalFakeComClientException 
	{		
		fail("Not yet implemented");	
	}

	@Test
	public void testCommit() throws FileNotFoundException, ComClientException, LocalFakeComClientException
	{
		Map<String, InputStream> map = new TreeMap<String, InputStream>();
		LocalComClientTest.fillMap(pathWS, pathWS, map);
		
		ComClient com = new LocalFakeComClient(pathServ);
		fail("Not implemented");
	//	com.commit("", "Uma mensagem estranha aqui", map);
	}

	@Test
	public void testInit() {
		fail("Not yet implemented");
	}

}
