package br.uff.ic.gardener.client;

/**
 * Implement the general exception of API client
 *
 * @author Marcos Cortes
 *
 */
public class InternalAPIClientException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -6034615067238713979L;

    /**
     * Constructs ...
     *
     *
     * @param msg
     * @param ex
     */
    public InternalAPIClientException(String msg, Exception ex) {
        super(msg, ex);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
