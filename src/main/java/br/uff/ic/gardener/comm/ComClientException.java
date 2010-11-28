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
	public ComClientException(String msg, Throwable parent)
	{
		this(msg, getDefaultCmd(), null, parent);
	}
	
	public static String getDefaultCmd()
	{
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		String cmd = "none";
		if(stack.length > 0)
			cmd = stack[stack.length-1].getMethodName();
		return cmd;

	}

}
