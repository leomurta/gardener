package br.uff.ic.gardener.patch.parser;

import br.uff.ic.gardener.patch.chunk.Chunk.Action;
import br.uff.ic.gardener.patch.delta.Delta;

import java.io.InputStream;

import java.util.LinkedList;

/**
 *
 * @author Daniel
 */
public class NormalParser extends BasicParser implements Parser {

    /**
     *
     * @param deltas
     * @return
     *
     * @throws ParserException
     */
    @Override
    public LinkedList<Result> parseDeltas(InputStream deltas) throws ParserException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Method description
     *
     *
     * @param delta
     *
     * @return
     *
     * @throws ParserException
     */
    @Override
    public Delta parseDelta(InputStream delta) throws ParserException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Method description
     *
     */
    @Override
    protected void setupSymbols() {
        super.setupSymbols();
        addSymbol("+", Action.ADDED);
        addSymbol("-", Action.DELETED);
        addSymbol("//", Action.MODIFIED);
        addSymbol("m", Action.MOVED);
        addSymbol(" ", Action.CONTEXT);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
