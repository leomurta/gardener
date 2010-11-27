package br.uff.ic.gardener.merge;

public class FileNotFoundException extends MergeException {
	private static final long serialVersionUID = 1L;

	public FileNotFoundException(String string) {
		super(string);
	}
	
	public FileNotFoundException(String string, Throwable throwable) {
		super(string, throwable);
	} 
}

