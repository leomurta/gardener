package br.uff.ic.gardener.comm.local;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
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
import br.uff.ic.gardener.util.FileHelper;
import br.uff.ic.gardener.workspace.WorkspaceTest;

public class LocalComClientTest {

	
	static
	ComClient comm = null;
	
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
			comm = new LocalComClient(pathServ.toURI());
			
			WorkspaceTest.createWorkspaceStructDirAndFiles(pathWS, 2, 2, 2, false);
		}catch(Exception e)
		{
			pathServ = null;
			pathWS = null;
			comm = null;
		}
	}

	
	@AfterClass
	public static void tearDownAfterClass()
	{
		try
		{
			FileHelper.deleteDirTree(pathWS);
			FileHelper.deleteDirTree(pathServ);
			comm.close();
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

	static public void fillMap(File radixPath, File path, Map<String, InputStream> map) throws FileNotFoundException
	{
		if(path.isDirectory())
		{
			for(File f: path.listFiles())
				fillMap(radixPath, f, map);
			
		}else if(path.isFile())
		{
			map.put(FileHelper.getRelative(radixPath, path).toString(), new FileInputStream(path));
		}
		
	}
	

	@Test
	public void testCheckout() throws ComClientException, FileNotFoundException 
	{
		Map<String, InputStream> map = new TreeMap<String, InputStream>();
		fillMap(pathWS, pathWS, map);
		
		//this.comm.checkout(RevisionID.LAST_REVISION, map);
		
	}

	@Test
	public void testCommit() throws FileNotFoundException, ComClientException {
		Map<String, InputStream> map = new TreeMap<String, InputStream>();
		fillMap(pathWS, pathWS, map);
		fail("Not implemented");
	//	comm.commit("","Teste ", map);
	}

	@Test
	public void testGetURIServ() {
		fail("Not yet implemented");
	}

	@Test
	public void testClose() {
		fail("Not yet implemented");
	}

}
