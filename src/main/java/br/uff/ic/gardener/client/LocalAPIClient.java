package br.uff.ic.gardener.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;

import br.uff.ic.gardener.TransationException;

class LocalAPIClient implements APIClient {

	public void checkout(Map<String, InputStream> items, long revision) {
		// TODO Auto-generated method stub

	}

	public void commit(Collection<String> eraseSubItems,
			Map<String, OutputStream> includeSubItems,
			Map<String, InputStream> modifyItems) throws TransationException {
		// TODO Auto-generated method stub
		
	}

	public void checkout(Collection<String> eraseSubItems,
			Map<String, InputStream> includeSubItems,
			Map<String, InputStream> modifySubItems, long actualRevision,
			long requestRevision) throws TransationException {
		// TODO Auto-generated method stub

	}

}
