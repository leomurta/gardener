package br.uff.ic.gardener.client;

public class CreationAPIClientException extends InternalAPIClientException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -383235864292919415L;

	public CreationAPIClientException(String msg, Exception ex) {
		super(msg, ex);
	}
}
