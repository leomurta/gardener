package br.uff.ic.gardener;

public class TransationException extends Exception 
{
	public TransationException(String msg)
	{
		super(msg);
	}
	
	public TransationException(String msg, Exception source)
	{
		super(msg, source);
	}
}
