package br.uff.ic.gardener.client;

/**
 * Implement the general exception of API client
 * 
 * @author Marcos Côrtes
 * 
 */
public class InternalAPIClientException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6034615067238713979L;

	public InternalAPIClientException(String msg, Exception ex) {
		super(msg, ex);
	}
}
