package br.uff.ic.gardener.diff;

/**
 * Uma excecao do patch
 *
 * @author Daniel Heraclio
 *
 */
public class DiffException extends Exception {

    /**
     *
     */
    public static final String MSG_INVALIDOPTION = "Unsupported option";

    /**
     * Constructs ...
     *
     *
     * @param msg
     */
    public DiffException(String msg) {
        super(msg);
    }

    /**
     * Constructs ...
     *
     *
     * @param ex
     * @param msg
     */
    public DiffException(Exception ex, String msg) {
        super(msg);
        this.initCause(ex);
    }
}
