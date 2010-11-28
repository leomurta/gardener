package br.uff.ic.gardener.comm.localfake;

import java.net.URI;

import br.uff.ic.gardener.comm.ComClientException;

public class LocalFakeComClientException extends ComClientException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -383235864292919415L;

	public LocalFakeComClientException(String msg, String cmd, URI serv, Throwable ex) {
		super(msg,cmd, serv, ex);
	}
	
	public LocalFakeComClientException(String msg, Throwable ex)
	{
		super(msg, ex);
		
	}
}

