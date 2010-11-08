package br.uff.ic.gardener.client;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

import br.uff.ic.gardener.comm.ComClient;
import br.uff.ic.gardener.comm.local.LocalComClient;
import br.uff.ic.gardener.comm.ComFactory;
import br.uff.ic.gardener.util.FileHelper;
import br.uff.ic.gardener.util.TestWithTemporaryPath;

public class ClientFactoryTest extends TestWithTemporaryPath {

	@Test
	public final void testCreateAPIClient() throws Exception {
		File file = FileHelper.createTemporaryRandomPath();
		
		//primeiro com Local
		ComClient comClient = ComFactory.createComClient(file.toURI(), "");
		Assert.assertTrue(comClient != null && comClient instanceof LocalComClient);
		
		//agora com LocalFake
		file = FileHelper.createTemporaryRandomPath();
		URI uri = file.toURI();
		if (uri.getHost() == null
				|| uri.getHost() == "") {
			try {
				uri = new URI(
						"file",
						"", 
						uri.getPath(),
						uri.getFragment()
						);
			} catch (URISyntaxException e) {
			}
		}
		
		
		
	}

}
