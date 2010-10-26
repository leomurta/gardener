package br.uff.ic.gardener.patch.parser;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.chunk.Chunk.Action;
import br.uff.ic.gardener.patch.delta.Delta;

//~--- JDK imports ------------------------------------------------------------

import java.io.InputStream;

import java.util.LinkedList;

/**
 *
 * @author Daniel
 */
public class ContextParser extends BasicParser implements Parser {
    @Override
    public LinkedList<Result> parseDeltas(InputStream deltas) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Delta parseDelta(InputStream delta) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

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
