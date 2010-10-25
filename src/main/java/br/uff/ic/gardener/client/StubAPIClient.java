package br.uff.ic.gardener.client;

import java.io.InputStream;
import java.util.Map;

import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.TransationException;

public class StubAPIClient implements APIClient {


	@Override
	public RevisionID getLastRevision() {

		return RevisionID.ZERO_REVISION;
	}
	
	
	
	@Override
	public RevisionID commit(Map<String, InputStream> items) throws TransationException {
			return RevisionID.ZERO_REVISION;
	}
	
	@Override
	public void checkout(Map<String, InputStream> items, RevisionID revision) throws TransationException {	
	}

}
