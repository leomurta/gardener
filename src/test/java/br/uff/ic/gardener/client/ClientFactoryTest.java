package br.uff.ic.gardener.client;

import static org.junit.Assert.*;

import org.junit.Test;

import br.uff.ic.gardener.TestWithTemporaryPath;

public class ClientFactoryTest extends TestWithTemporaryPath {



	@Test
	public final void testCreateAPIClient() {
		try {
			ClientFactory.createAPIClient(getPath().toString());
		} catch (CreationAPIClientException e) {
			fail("Não conseguiu criar a API Client");
		}
	}

}
