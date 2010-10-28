package br.uff.ic.gardener.patch.parser;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.delta.Delta;

//~--- JDK imports ------------------------------------------------------------

import java.io.InputStream;

import java.util.LinkedList;

/**
 *
 * @author Daniel
 */
public interface Parser {

    /**
     * Parse stream containing deltas end return collection of Delta
     * @param deltas
     * @return
     * @throws Exception
     */
    public LinkedList<Result> parseDeltas(InputStream deltas) throws Exception;

    /**
     * Parse stream containing a delta end returns a delta
     * @param delta
     * @return
     * @throws Exception
     */
    public Delta parseDelta(InputStream delta) throws Exception;

    /**
     *
     * @param deltas
     * @return
     */
    public String toString(LinkedList<Delta> deltas);

}


//~ Formatted by Jindent --- http://www.jindent.com
