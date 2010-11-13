package br.uff.ic.gardener.patch.patcher;

import br.uff.ic.gardener.patch.*;

import java.io.IOException;

/**
 * Uma excecao do patcher
 *
 * @author Daniel Heraclio
 *
 */
public class PatcherException extends PatchException {

    /** Field description */
    public static final String MSG_INVALIDACTION = "Unsupported chunk action";

    /** Field description */
    public static final String MSG_INVALIDMATCH = "Unsupported matching type";

    /** Field description */
    public static final String MSG_MATCHERROR = "Matching error";

    /**
     * Default message for patcher errors.
     */
    public static final String MSG_PATCHER_DEFAULT = "Patcher exception";

    /**
     * Constructs ...
     *
     */
    public PatcherException() {
        super(MSG_PATCHER_DEFAULT);
    }

    /**
     * Constructs ...
     *
     *
     * @param ex
     */
    public PatcherException(Exception ex) {
        super(ex, MSG_PATCHER_DEFAULT);
    }

    /**
     * Constructs ...
     *
     *
     * @param msg
     */
    public PatcherException(String msg) {
        super(msg);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
