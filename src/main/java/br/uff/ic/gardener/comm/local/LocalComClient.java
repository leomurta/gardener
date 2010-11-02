package br.uff.ic.gardener.comm.local;


import java.io.InputStream;
import java.util.Map;

import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.TransationException;
import br.uff.ic.gardener.comm.ComClient;

/**
 * Implementa a comunicação usando servidor local
 * @author Vitor
 *
 */
class LocalComClient implements ComClient {

	@Override
	public void checkout(RevisionID revision, Map<String, InputStream> items)
			throws TransationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RevisionID commit(Map<String, InputStream> items)
			throws TransationException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
