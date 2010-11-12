package br.uff.ic.gardener.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import br.uff.ic.gardener.workspace.WorkspaceTest;

public class FileHelperTest {

	@Test
	public void testDeleteDirTree() {
		File path = null;
		try {
			path = FileHelper.createTemporaryRandomPath();
			WorkspaceTest.createWorkspaceStructDirAndFiles(path, 4, 4, 4, true);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Cannot create temporary path and create dummy paths");
		} 
		
		FileHelper.deleteDirTree(path);
		
		assertTrue(!path.exists());
	}

	@Test
	public void testCreateTemporaryRandomFile() throws IOException {
		File f = FileHelper.createTemporaryRandomFile();
		assertTrue(f.exists());
	}

	@Test
	public void testGetFileFromURI() {
		File file = null;
		try {
			file = FileHelper.createTemporaryRandomFile();
		} catch (IOException e) {
			fail("Cannot execute FileHelper.createTemporaryRandomFile()");
		}

		URI uri;
		File two = null;
		try {
			uri = new URI("http:///" + file.toURI().getPath());
			two = FileHelper.getFileFromURI(uri);
		} catch (URISyntaxException e) {
			fail("Cannot execute new URI(");
		}

		assertEquals(file, two);

		file.delete();

		//////////////////

		try {
			uri = new URI("http://www.gmail.com//");
			file = FileHelper.getFileFromURI(uri);
			//assertEquals(file, null);
		} catch (URISyntaxException e) {
			fail("Exception not expected");
		}

		assertEquals(file, null);
	}

}