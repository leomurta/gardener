package br.uff.ic.gardener.client;

import java.io.File;

/**
 * Factory to create the specific API Client. It depends of configuration.
 * 
 * @author Marcos Côrtes
 * @param param
 *            parameter dependent of APIClient created. TODO: Create the
 *            configuration file to determined the configuration of factory
 */
public class ClientFactory {

	public static APIClient createAPIClient(String param)
			throws CreationAPIClientException {
		// String str = System.getProperty("user.dir");
		// File dir = new File(str);
		return new LocalAPIClient(new File(param));
	}
}
