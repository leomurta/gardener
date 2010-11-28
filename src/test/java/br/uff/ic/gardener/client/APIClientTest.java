/**
 * 
 */
package br.uff.ic.gardener.client;


import java.io.File;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.util.FileHelper;
import br.uff.ic.gardener.workspace.Workspace;
import br.uff.ic.gardener.workspace.WorkspaceException;
import br.uff.ic.gardener.workspace.WorkspaceTest;

/**
 * @author Marcos
 *
 */
public class APIClientTest {

	//Workspace workspace = null;
	APIClient client = null;
	
	File fWS = null;
	File fServ = null;
	
	private static String getStrServ(File pathServ2)
	{
		String tempStrPath = pathServ2.getPath();
		tempStrPath = tempStrPath.replace(File.separatorChar, '/');
		return String.format("filefake:///%s", tempStrPath);
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		fWS = FileHelper.createTemporaryRandomPath();
		fServ = FileHelper.createTemporaryRandomPath();
		client = new APIClient(fWS, new URI(getStrServ(fServ)));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		client.forceClose();
		FileHelper.deleteDirTree(fWS);
		FileHelper.deleteDirTree(fServ);
	}
	
	/**
	 * Testa o status
	 */
	@Test
	public void SceneStatus() throws Exception
	{
		client.init("noname");
		
	//	WorkspaceTest.createWorkspaceStructDirAndFiles(fWS, 2, 2, 2, false);
		
		File f1 = new File(fWS, "1.txt");
		FileHelper.fillFile(f1,"1","2", "3");
		List<File> list = new LinkedList<File>();
		//FileHelper.findFiles(fWS, list, "**", true);
		list.add(f1);
		client.addFiles(list);
		client.commit("primeiro commit");
		
		File f2 = new File(fWS, "2.txt");
		FileHelper.fillFile(f2,"1","2","3");
		
		list.clear();
		list.add(f2);
		client.addFiles(list);
		client.commit("segundo commit");
		
		
		//cria outro workspace
		File ws2 = FileHelper.createTemporaryRandomPath();
		try{		
			APIClient client2 = new APIClient(ws2, new URI(getStrServ(fServ)));
			client2.checkout(RevisionID.LAST_REVISION);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		//faz uma mod
		FileHelper.fillFile(f2, "1","a","3");
		client.commit("terceiro commit");
		
		//faz o update
		{
			APIClient client2 = new APIClient(ws2, new URI(getStrServ(fServ)));
			List<Conflict> listC = new LinkedList<Conflict>();
			client2.update(listC);			
		}
		
		
		
		
	}

}
