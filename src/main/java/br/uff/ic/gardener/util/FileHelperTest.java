package br.uff.ic.gardener.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

public class FileHelperTest {

	@Test
	public void testDeleteDirTree() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateTemporaryRandomFile() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateFile() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindFiles() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateTemporaryRandomPath() {
		fail("Not yet implemented");
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
