package br.uff.ic.gardener.patch.parser;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.delta.Delta;

//~--- JDK imports ------------------------------------------------------------

import java.io.FileInputStream;

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
    public LinkedList<Result> parse(FileInputStream deltas) throws Exception;

    /**
     *
     * @param deltas
     * @return
     */
    public String toString(LinkedList<Delta> deltas);
}


//~ Formatted by Jindent --- http://www.jindent.com
