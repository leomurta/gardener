package br.uff.ic.gardener.workspace;


import java.io.File;

import org.junit.Before;
import org.junit.Test;

import br.uff.ic.gardener.util.TestWithTemporaryPath;

public class WorkspaceConfigParserTest extends TestWithTemporaryPath{

	public Workspace workspace = null;
	public WorkspaceConfigParser wParser = null;
	
	@Before
	public void setUp() throws WorkspaceException, WorkspaceConfigParserException
	{
		File pathWorkspace = this.folder.newFolder("workspace");
		
		workspace = new Workspace(pathWorkspace);
		wParser = new WorkspaceConfigParser(workspace, pathWorkspace);
	}
	
	
	
	@Test
	public void testSave() throws WorkspaceConfigParserException
	{
	//	List<CIWorkspace> list = new LinkedList<CIWorkspace>();
		wParser.save();
	
	}
}
