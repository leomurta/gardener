package br.uff.ic.gardener.patch;

/**
 * Uma excecao do patch
 *
 * @author Daniel Heraclio
 *
 */
public class PatchException extends Exception {

    /**
     *
     */
    public static final String MSG_INVALIDFORMAT = "Unsupported format";

    /**
     *
     */
    public static final String MSG_INVALIDTYPE = "Unsupported Type";

    /**
     * Constructs ...
     *
     *
     * @param msg
     */
    public PatchException(String msg) {
        super(msg);
    }

    /**
     * Constructs ...
     *
     *
     * @param ex
     * @param msg
     */
    public PatchException(Exception ex, String msg) {
        super(msg);
        this.initCause(ex);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
