package br.uff.ic.gardener;

public class TransationException extends Exception {
	/**
	 * @deprecated Não é muito informativa esta Exceção.
	 */
	private static final long serialVersionUID = -3874522250471370406L;

	public TransationException(String msg) {
		super(msg);
	}

	public TransationException(String msg, Exception source) {
		super(msg, source);
	}
}
