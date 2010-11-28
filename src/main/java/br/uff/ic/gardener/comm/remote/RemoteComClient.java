package br.uff.ic.gardener.comm.remote;

import java.net.URI;
import java.util.Collection;

import br.uff.ic.gardener.ConfigurationItem;
import br.uff.ic.gardener.RevisionCommited;
import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.comm.ComClient;
import br.uff.ic.gardener.comm.ComClientException;

public class RemoteComClient implements ComClient {

	public RemoteComClient(URI uri)
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
	public void init(String strProject) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public RevisionID getLastRevision(String strProject) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public RevisionID checkout(String strProject, RevisionID revision,
			Collection<ConfigurationItem> items) throws ComClientException {
		
		return RevisionID.ZERO_REVISION;
		
	}


	@Override
	public RevisionID commit(String strProject, String strMessage,
			String strUser, Collection<ConfigurationItem> items)
			throws ComClientException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void generateLog(Collection<RevisionCommited> coll,
			RevisionID firstRevision, RevisionID lastRevision)
			throws ComClientException {
		// TODO Auto-generated method stub
		
	}

}
