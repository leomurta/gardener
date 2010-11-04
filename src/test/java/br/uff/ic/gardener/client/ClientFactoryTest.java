package br.uff.ic.gardener.client;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import br.uff.ic.gardener.comm.ComClient;
import br.uff.ic.gardener.comm.ComFactory;
import br.uff.ic.gardener.util.TestWithTemporaryPath;

public class ClientFactoryTest extends TestWithTemporaryPath {

	@Test
	public final void testCreateAPIClient() throws Exception {
		File file = folder.newFolder("teste");
		
		//primeiro com file
		ComClient comClient = ComFactory.createComClient(file.toURI(), "");
		
		Assert.assertTrue(comClient != null);
	}

}
