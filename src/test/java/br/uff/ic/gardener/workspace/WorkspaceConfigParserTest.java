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
