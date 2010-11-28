package br.uff.ic.gardener.merge;

public class MergeException extends Exception {

	private static final long serialVersionUID = 1L;

	public MergeException(String string) {
		super(string);
	}
	
	public MergeException(String string, Throwable throwable) {
		super(string, throwable);
	} 
}
