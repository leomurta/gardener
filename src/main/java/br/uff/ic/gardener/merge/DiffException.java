package br.uff.ic.gardener.merge;

public class DiffException extends MergeException {

	private static final long serialVersionUID = 1L;

	public DiffException(String string) {
		super(string);
	}
	
	public DiffException(String string, Throwable throwable) {
		super(string, throwable);
	} 
}
