package br.uff.ic.gardener.client;

public class APIClientException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public APIClientException(String msg, Exception e)
	{
		super(msg + ". Source: " + ((e!=null)?e.toString():"none"), e);
	}
}
