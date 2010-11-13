package br.uff.ic.gardener.patch.parser;

import br.uff.ic.gardener.patch.*;

/**
 * Uma excecao do parser
 *
 * @author Daniel Heraclio
 *
 */
public class ParserException extends PatchException {

    /** Field description */
    public static final String MSG_INVALIDLINE = "Invalid line";

    /** Field description */
    public static final String MSG_INVALIDPATCHHEADER = "Invalid patch: header incomplete";

    /**
     * Default message for parser errors.
     */
    public static final String MSG_PARSER_DEFAULT = "Parser exception";

    /**
     * Constructs ...
     *
     */
    public ParserException() {
        super(MSG_PARSER_DEFAULT);
    }

    /**
     * Constructs ...
     *
     *
     * @param ex
     */
    public ParserException(Exception ex) {
        super(ex, MSG_PARSER_DEFAULT);
    }

    /**
     * Constructs ...
     *
     *
     * @param msg
     */
    public ParserException(String msg) {
        super(msg);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
