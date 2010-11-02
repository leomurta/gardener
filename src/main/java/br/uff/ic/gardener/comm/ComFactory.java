package br.uff.ic.gardener.comm;

import java.io.File;

import br.uff.ic.gardener.comm.localfake.LocalFakeComClient;


/**
 * Factory to create the specific API Client. It depends of configuration.
 * 
 * @author Vitor Neves
 * @param param
 *            parameter dependent of APIClient created.
 *            TODO: Create the configuration file to determined the configuration of factory
 */
public class ComFactory {
	public static ComClient createComClient(String param)
	throws Exception {
	// String str = System.getProperty("user.dir");
	// File dir = new File(str);
	return new LocalFakeComClient(new File(param));
	
	}
}
