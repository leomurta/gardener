package br.uff.ic.gardener.workspace;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.util.TestWithTemporaryPath;

public class WorkspaceConfigParserTest extends TestWithTemporaryPath{

	public Workspace workspace = null;
	public WorkspaceConfigParser wParser = null;
	
	@Before
	public void setUp() throws WorkspaceException
	{
		File pathWorkspace = this.folder.newFolder("workspace");
		
		workspace = new Workspace(pathWorkspace);
		wParser = new WorkspaceConfigParser(workspace, pathWorkspace);
	}
	
	
	
	@Test
	public void testSave() throws WorkspaceConfigParserException
	{
		List<CIWorkspace> list = new LinkedList<CIWorkspace>();
		wParser.saveProfile(list);
	
	}
	@Test
	public void testLoad() throws WorkspaceConfigParserException, FileNotFoundException
	{
		File pathWorkspace = this.folder.newFolder("workspace");
		OutputStream osProfile = new FileOutputStream(new File(pathWorkspace, WorkspaceConfigParser.STR_FILE_PROFILE));
		PrintStream psProfile = new PrintStream(osProfile, true);
		psProfile.println("SERV_ORIGIN 	null");
		psProfile.println("REVISION " + RevisionID.ZERO_REVISION.toString());
		psProfile.println("LAST_TIMESTAMP_CHECKOUT       \t\t\t \"4234\"");
		psProfile.close();
		LinkedList<CIWorkspace> list = new LinkedList<CIWorkspace>();
		wParser.loadProfile(list);
		
		Assert.assertEquals("Carregamento de arquivo não foi bem sucedido", new Date(4234), workspace.getCheckoutTime());
		Assert.assertEquals("Carregamento de arquivo não foi bem sucedido", null, workspace.getServSource());
		Assert.assertEquals("Carregamento de arquivo não foi bem sucedido", RevisionID.ZERO_REVISION, workspace.getCurrentRevision());
	}
	
	@Test
	public void testLoadOperations() throws WorkspaceConfigParserException, FileNotFoundException, URISyntaxException
	{
		File pathWorkspace = this.folder.newFolder("workspace");
		OutputStream os = new FileOutputStream(new File(pathWorkspace, WorkspaceConfigParser.STR_FILE_OPERATION));
		PrintStream ps = new PrintStream(os, true);
		
		List<CIWorkspaceStatus> list = new LinkedList<CIWorkspaceStatus>();
		
		list.add(new CIWorkspaceStatus(new URI("a.txt"), null, Status.ADD));
		list.add(new CIWorkspaceStatus(new URI("b.txt"), null, Status.ADD));
		list.add(new CIWorkspaceStatus(new URI("c.txt"), null, Status.REM));
		list.add(new CIWorkspaceStatus(new URI("d.txt"), null, Status.RENAME, null, new URI("e.txt")));
		
		for(CIWorkspaceStatus ci: list)
		{
			ps.println(ci.toString());
		}
		ps.close();
	
		List<CIWorkspaceStatus> list2 = new LinkedList<CIWorkspaceStatus>();
		wParser.loadOperations(list2);
		
		Assert.assertEquals(list, list2);
	}
	
	@Test
	public void testAppendOperations() throws FileNotFoundException, WorkspaceConfigParserException
	{
		File pathWorkspace = folder.newFolder("workspace");
		OutputStream os = new FileOutputStream(new File(pathWorkspace, WorkspaceConfigParser.STR_FILE_OPERATION));
		
		CIWorkspaceStatus[] array = null;
		try {
		array = new CIWorkspaceStatus[]{
		/*0*/	new CIWorkspaceStatus(new URI("a.bat"), null,Status.ADD),
		/*1*/	new CIWorkspaceStatus(new URI("b.bat"), null,Status.ADD),
		/*2*/	new CIWorkspaceStatus(new URI("c.bat"), null,Status.REM),
		/*3*/	new CIWorkspaceStatus(new URI("d.bat"), null,Status.RENAME, new Date(), new URI("e.txt")),
		/*4*/	new CIWorkspaceStatus(new URI("x.bat"), null,Status.ADD),
		/*5*/	new CIWorkspaceStatus(new URI("y.bat"), null,Status.ADD),
		/*6*/	new CIWorkspaceStatus(new URI("w.bat"), null,Status.REM),
		/*7*/	new CIWorkspaceStatus(new URI("z.bat"), null,Status.REM),
		/*8*/	new CIWorkspaceStatus(new URI("z.bat"), null,Status.RENAME, new Date(), new URI("q.bat"))
		};
		
		PrintStream ps = new PrintStream(os, true);
		for(int i = 0; i < 4; i++)
		{
			ps.println(array[i].toString());
		}
		ps.close();
		
		ArrayList<CIWorkspaceStatus> list = new ArrayList<CIWorkspaceStatus>();
		
			for(int i = 4; i < array.length; i++ )
			{
				list.add(array[i]);
			}
			wParser.appendOperations(list);
			wParser.loadOperations(list);
			
			for(int i = 0; i < array.length; i++)
			{
				Assert.assertEquals(array[i], list.get(i));
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail("Cannot parse URI");
		}
	}
	
	@Test
	public void loadRealICContentTest() throws IOException, WorkspaceConfigParserException
	{
		WorkspaceTest.createWorkspaceStructDirAndFiles(workspace.getPath(), 3, 3, 3, true);
		
		LinkedList<CIWorkspaceStatus> list  =  new LinkedList<CIWorkspaceStatus>();
		wParser.loadRealICContent(list);
		
		for(CIWorkspaceStatus ci: list)
		{
			File f = new File(workspace.getPath(), ci.getStringID());
			Assert.assertTrue("Expected file not found: " + f.toString(),f.exists());
		}
	}
}
