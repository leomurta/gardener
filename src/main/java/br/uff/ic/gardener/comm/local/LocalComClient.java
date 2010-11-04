package br.uff.ic.gardener.comm.local;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
	public void checkout(RevisionID revision, Map<String, InputStream> items)
			throws ComClientException {
		//Server.getInstance().checkout()
		
	}

	@Override
	public RevisionID commit(String strProject, String strMessage, Map<String, InputStream> items)
			throws ComClientException {
		//transforma tudo em arquivo temporário
		File pathTemp = null;
		try {
			pathTemp = FileHelper.createTemporaryRandomPath();
			
			List<File> listFiles = new ArrayList<File>();
			for(Map.Entry<String, InputStream> p: items.entrySet())
			{
				File file = FileHelper.createFile(pathTemp, p.getKey());
				UtilStream.copy(p.getValue(), new FileOutputStream(file));
				listFiles.add(file);
			}
		
			File[] files = new File[listFiles.size()];
			Server.getInstance().checkIn(strProject, "", (new Date()).toString(), strMessage, "", listFiles.toArray(files));
			Server.getInstance().commitCheckin(strProject);
			FileHelper.deleteDirTree(pathTemp);
			
		}catch(IOException e)
		{
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
	
	
	
	
}
