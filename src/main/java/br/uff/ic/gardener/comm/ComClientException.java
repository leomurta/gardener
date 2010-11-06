package br.uff.ic.gardener.comm;

import java.net.URI;

public class ComClientException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4276839626099082716L;

	private URI uriServ = null;
	
	private String strCommand = null;
	
	final URI getURIServ()
	{
		return uriServ;
	}
	
	final String getCommand()
	{
		return strCommand;
	}
	
	public ComClientException(String msg, String _strCommand, URI _uriServ, Throwable parent)
	{
		super(msg, parent);
		uriServ = _uriServ;
		strCommand = _strCommand;
	}

}
