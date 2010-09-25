package br.uff.ic.gardener.client;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import br.uff.ic.gardener.TestWithTemporaryPath;

public class LocalAPIClientTest extends TestWithTemporaryPath {

	LocalAPIClient client;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public final void testLocalAPIClient() {
		try {
			client = new LocalAPIClient(getPath());
		} catch (CreationAPIClientException e) {
			e.printStackTrace();
			fail("Cannot create the exception: " + e.getMessage());
		}
	}

	public final void testCheckout() {

		// TODO: implements
	}

	@Test
	public final void testCommit() {
		// TODO: implements
	}

	@Test
	public final void testGetLastRevision() {
		// TODO: implements
	}

}
