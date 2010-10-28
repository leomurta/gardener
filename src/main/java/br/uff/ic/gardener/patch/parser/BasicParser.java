package br.uff.ic.gardener.patch.parser;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.chunk.Chunk;
import br.uff.ic.gardener.patch.chunk.Chunk.Action;
import br.uff.ic.gardener.patch.delta.Delta;
import br.uff.ic.gardener.util.MapHelper;
import br.uff.ic.gardener.util.TextHelper;

//~--- JDK imports ------------------------------------------------------------

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author Daniel
 */
public class BasicParser {
    private Map<String, Chunk.Action> symbols;

    protected BasicParser() {
        setupSymbols();
    }

    /**
     *
     * @param deltas
     * @return String
     */
    public String toString(LinkedList<Delta> deltas) {
        return TextHelper.toString(deltas);
    }

    protected void setupSymbols() {
        setSymbols(new LinkedHashMap<String, Action>(5));
    }

    protected void addSymbol(String key, Chunk.Action value) {
        getSymbols().put(key, value);
    }

    /**
     * @return the symbols
     */
    protected Map<String, Chunk.Action> getSymbols() {
        return symbols;
    }

    /**
     * @param symbols the symbols to set
     */
    protected void setSymbols(Map<String, Chunk.Action> symbols) {
        this.symbols = symbols;
    }

    protected Action getAction(String action) throws Exception {
        if ((action == null) || (!getSymbols().containsKey(action))) {
            throw new Exception("Implementation error");
        }

        return getSymbols().get(action);
    }

    /**
     * Converts action to string according to symbols provided by the setupSymbols method.
     * @param action
     * @return
     */
    public String toString(Action action) throws Exception {
        if ((action == null) || (!getSymbols().containsValue(action))) {
            throw new Exception("Implementation error");
        }

        return (String) MapHelper.getKeyFromValue(getSymbols(), action);
    }

    protected String clearBreakLines(String text) {
        return text.replace("\r", "").replace("\n", "");
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
