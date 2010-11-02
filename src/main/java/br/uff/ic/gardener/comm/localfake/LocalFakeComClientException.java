package br.uff.ic.gardener.comm.localfake;

public class LocalFakeComClientException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -383235864292919415L;

	public LocalFakeComClientException(String msg, Exception ex) {
		super(msg, ex);
	}
}

