package br.uff.ic.gardener.comm.local;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


import br.uff.ic.gardener.ConfigurationItem;
import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.comm.ComClient;
import br.uff.ic.gardener.comm.ComClientException;
import br.uff.ic.gardener.server.Server;
import br.uff.ic.gardener.util.FileHelper;
import br.uff.ic.gardener.util.UtilStream;

/**
 * @author Vitor
 *
 */
public class LocalComClient implements ComClient {

	URI uriServ = null;
	
	public LocalComClient(URI _uriServ)
	{
		uriServ = _uriServ;
	}
	
	@Override
	public void checkout(String strProject, RevisionID revision, Collection<ConfigurationItem> items)
			throws ComClientException {
		throw new ComClientException("Not implemented","checkout", null, null );
		
	}

	@Override
	public RevisionID commit(String strProject, String strMessage, Collection<ConfigurationItem> items)
			throws ComClientException {
		
		File pathTemp = null;
		try {
			pathTemp = FileHelper.createTemporaryRandomPath();
			ArrayList<File> listFiles = new ArrayList<File>();
			for(ConfigurationItem p: items)
			{
				File file = FileHelper.createFile(pathTemp, p.getStringID());
				UtilStream.copy(p.getItemAsInputStream(), new FileOutputStream(file));
				listFiles.add(file);
			}
			
			Server.getInstance().ckeckIn(strProject, "", (new Date()).toString(), strMessage, "", listFiles);
			Server.getInstance().commitCheckin(strProject);
			FileHelper.deleteDirTree(pathTemp);
		} catch (IOException e) {
			if(pathTemp != null)
				FileHelper.deleteDirTree(pathTemp);
			throw new ComClientException("Não foi possível transformar os inputstreams para files temporários", "commit", getURIServ(), e);
		}
		
		long r = Server.getInstance().getLastRevision(strProject);
		return new RevisionID(r);
	}

	@Override
	public URI getURIServ() 
	{
		return uriServ;
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
		return RevisionID.ZERO_REVISION;
	}
	
	
	
	
}
