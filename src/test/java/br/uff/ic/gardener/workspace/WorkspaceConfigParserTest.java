package br.uff.ic.gardener.workspace;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.uff.ic.gardener.ConfigurationItem;
import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.client.StubAPIClient;
import br.uff.ic.gardener.util.TestWithTemporaryPath;
import br.uff.ic.gardener.util.UtilStream;
import br.uff.ic.gardener.workspace.WorkspaceOperation.Operation;

public class WorkspaceConfigParserTest extends TestWithTemporaryPath{

	public Workspace workspace = null;
	public WorkspaceConfigParser wParser = null;
	
	@Before
	public void setUp()
	{
		File pathWorkspace = this.folder.newFolder("workspace");
		
		workspace = new Workspace(pathWorkspace, new StubAPIClient());
		wParser = new WorkspaceConfigParser(workspace, pathWorkspace);
	}
	
	@Test
	public void testURI() throws IOException, URISyntaxException
	{
		File file = this.folder.newFile("teste.txt");
		UtilStream.fillFile(file, "aaa", "bbb", "ccc", "ddd");
		String temp = "file:///"+file.toString();
		temp = temp.replace('\\', '/');
		URL url = new URL(temp);
		URI uri = new URI(temp);
		String strPath = uri.getPath();
		String strHost = uri.getHost();
		String strAutority = uri.getAuthority();
		String strFragment = uri.getFragment();
		String strQuery = uri.getQuery();
		String strPort = Integer.toString(uri.getPort());
		String strScheme = uri.getScheme();
		String strSpecifyScheme = uri.getSchemeSpecificPart();
		String strUserInfo = uri.getUserInfo();
	}
	
	@Test
	public void testSave() throws WorkspaceConfigParserException
	{
		wParser.saveProfile();
	
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
		
		ArrayList<ConfigurationItem> array = new ArrayList<ConfigurationItem>();
		wParser.loadProfile(array);
		
		Assert.assertEquals("Carregamento de arquivo não foi bem sucedido", new Date(4234), workspace.getCheckoutTime());
		Assert.assertEquals("Carregamento de arquivo não foi bem sucedido", null, workspace.getServSource());
		Assert.assertEquals("Carregamento de arquivo não foi bem sucedido", RevisionID.ZERO_REVISION, workspace.getCurrentRevision());
	}
	
	@Test
	public void testLoadOperations() throws WorkspaceConfigParserException, FileNotFoundException
	{
		File pathWorkspace = this.folder.newFolder("workspace");
		OutputStream os = new FileOutputStream(new File(pathWorkspace, WorkspaceConfigParser.STR_FILE_OPERATION));
		PrintStream ps = new PrintStream(os, true);
		ps.println("+++ a.txt");
		ps.println("+++ b.txt");
		ps.println("--- c.txt");
		ps.println("+-RRR d.txt e.txt");
		ps.close();
		
		ArrayList<WorkspaceOperation> list = new ArrayList<WorkspaceOperation>();
		wParser.loadOperations(list);
		
		Assert.assertEquals(list.get(0), new WorkspaceOperation(Operation.ADD_FILE, "a.txt"));
		Assert.assertEquals(list.get(1), new WorkspaceOperation(Operation.ADD_FILE, "b.txt"));
		Assert.assertEquals(list.get(2), new WorkspaceOperation(Operation.REMOVE_FILE, "c.txt"));
		Assert.assertEquals(list.get(3), new WorkspaceOperation(Operation.RENAME_FILE, "d.txt", "e.txt"));
	}
	
	@Test
	public void testAppendOperations() throws FileNotFoundException, WorkspaceConfigParserException
	{
		File pathWorkspace = folder.newFolder("workspace");
		OutputStream os = new FileOutputStream(new File(pathWorkspace, WorkspaceConfigParser.STR_FILE_OPERATION));
		PrintStream ps = new PrintStream(os, true);
		ps.println("+++ a.txt");
		ps.println("+++ b.txt");
		ps.println("--- c.txt");
		ps.println("+-RRR d.txt e.txt");
		ps.close();
		
		ArrayList<WorkspaceOperation> list = new ArrayList<WorkspaceOperation>();
		list.add(new WorkspaceOperation(Operation.ADD_FILE, "x.bat"));
		list.add(new WorkspaceOperation(Operation.ADD_FILE, "y.bat"));
		list.add(new WorkspaceOperation(Operation.REMOVE_FILE, "w.bat"));
		list.add(new WorkspaceOperation(Operation.REMOVE_FILE, "z.bat"));
		list.add(new WorkspaceOperation(Operation.RENAME_FILE, "p.bat", "q.bat"));
		wParser.appendOperations(list);
		
		wParser.loadOperations(list);
		Assert.assertEquals(list.get(0), new WorkspaceOperation(Operation.ADD_FILE, "a.txt"));
		Assert.assertEquals(list.get(1), new WorkspaceOperation(Operation.ADD_FILE, "b.txt"));
		Assert.assertEquals(list.get(2), new WorkspaceOperation(Operation.REMOVE_FILE, "c.txt"));
		Assert.assertEquals(list.get(3), new WorkspaceOperation(Operation.RENAME_FILE, "d.txt", "e.txt"));
		Assert.assertEquals(list.get(4), new WorkspaceOperation(Operation.ADD_FILE, "x.bat"));
		Assert.assertEquals(list.get(5), new WorkspaceOperation(Operation.ADD_FILE, "y.bat"));
		Assert.assertEquals(list.get(6), new WorkspaceOperation(Operation.REMOVE_FILE, "w.bat"));
		Assert.assertEquals(list.get(7), new WorkspaceOperation(Operation.REMOVE_FILE, "z.bat"));
		Assert.assertEquals(list.get(8), new WorkspaceOperation(Operation.RENAME_FILE, "p.bat", "q.bat"));		
		
	}
}
