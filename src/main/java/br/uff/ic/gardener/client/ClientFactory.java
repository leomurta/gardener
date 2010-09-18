package br.uff.ic.gardener.client;

/**
 * Factory to create the specific API Client. It depends of configuration.
 * @author Marcos Côrtes
 * TODO: Create the configuration file to determined the configuration
 */
public class ClientFactory {
	
	public static APIClient createAPIClient()
	{
		return new LocalAPIClient();
	}
}
