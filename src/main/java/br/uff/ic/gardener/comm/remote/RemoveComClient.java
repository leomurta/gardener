package br.uff.ic.gardener.comm.remote;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;

import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.comm.ComClient;
import br.uff.ic.gardener.comm.ComClientException;

public class RemoveComClient implements ComClient {

	public RemoveComClient(URI uri)
	{
		
	}
	 

	@Override
	public URI getURIServ() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}


	@Override
	public void checkout(RevisionID revision, Map<String, InputStream> items)
			throws ComClientException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public RevisionID commit(String strProject, String strMessage,
			Map<String, InputStream> items) throws ComClientException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void init(String strProject) {
		// TODO Auto-generated method stub
		
	}

}
