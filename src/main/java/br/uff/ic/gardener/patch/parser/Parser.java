package br.uff.ic.gardener.patch.parser;

import br.uff.ic.gardener.patch.delta.Delta;

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
     *
     *
     * @throws ParserException
     */
    public LinkedList<Result> parseDeltas(InputStream deltas) throws ParserException;

    /**
     * Parse stream containing a delta end returns a delta
     * @param delta
     * @return
     *
     *
     * @throws ParserException
     */
    public Delta parseDelta(InputStream delta) throws ParserException;

    /**
     *
     * @param deltas
     * @return
     */
    public String toString(LinkedList<Delta> deltas);
}


//~ Formatted by Jindent --- http://www.jindent.com
