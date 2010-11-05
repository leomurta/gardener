package br.uff.ic.gardener.comm;

import java.io.File;
import java.net.URI;

import br.uff.ic.gardener.comm.local.LocalComClient;
import br.uff.ic.gardener.comm.localfake.LocalFakeComClient;
import br.uff.ic.gardener.comm.remote.RemoteComClient;
import br.uff.ic.gardener.util.FileHelper;


/**
 * Factory to create the specific API Client. It depends of configuration.
 * 
 * @author Vitor Neves
 * @param param
 *            parameter dependent of APIClient created.
 *            TODO: Create the configuration file to determined the configuration of factory
 */
public class ComFactory {
	
	/**
	 * 
	 * @param uriServ Locator of serv
	 * @param param param configuration
	 * @return
	 * @throws Exception
	 */
	public static ComClient createComClient(URI uriServ, String param)
	throws Exception {
		
		final String strType = uriServ.getScheme();
		
		if("file".equalsIgnoreCase(strType))
		{
			return new LocalComClient(uriServ);
		}else if("filefake".equalsIgnoreCase(strType))
		{
			File f = FileHelper.getFileFromURI(uriServ);
			
			return new LocalFakeComClient(f);
		}else if("http".equalsIgnoreCase(strType))
		{
			return new RemoteComClient(uriServ);
		}else
		{
			throw new Exception("Não foi possível especificar qual tipo de ComClient deve ser criada: " + uriServ.toASCIIString());
		}
	}
}
