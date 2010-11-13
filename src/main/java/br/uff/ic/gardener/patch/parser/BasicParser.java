package br.uff.ic.gardener.patch.parser;

import br.uff.ic.gardener.patch.chunk.Chunk;
import br.uff.ic.gardener.patch.chunk.Chunk.Action;
import br.uff.ic.gardener.patch.delta.Delta;
import br.uff.ic.gardener.util.MapHelper;
import br.uff.ic.gardener.util.TextHelper;
import br.uff.ic.gardener.util.UtilStream;

import java.io.InputStream;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author Daniel
 */
public abstract class BasicParser {

    /** Missing new line marker */
    protected static final String CR = "\n";

    /** Missing new line marker */
    protected static final String LF = "\r";

    /** Missing new line marker */
    private static final String NEWLINE_HEADER = "\\";

    /** Missing new line marker */
    protected static final String LFCR = LF + CR;

    /** Field description */
    private Map<String, Chunk.Action> symbols;

    /**
     * Constructs ...
     *
     */
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

    /**
     *
     */
    protected void setupSymbols() {
        setSymbols(new LinkedHashMap<String, Action>(5));
    }

    /**
     * Method description
     *
     *
     * @param key
     * @param value
     */
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

    /**
     * Method description
     *
     *
     * @param action
     *
     * @return
     *
     *
     * @throws ParserException
     */
    protected Action getAction(String action) throws ParserException {
        if ((action == null) || (!getSymbols().containsKey(action))) {
            throw new ParserException();
        }

        return getSymbols().get(action);
    }

    /**
     * Converts action to string according to symbols provided by the setupSymbols method.
     * @param action
     * @return
     *
     *
     * @throws ParserException
     */
    public String toString(Action action) throws ParserException {
        if ((action == null) || (!getSymbols().containsValue(action))) {
            throw new ParserException();
        }

        try {
            return (String) MapHelper.getKeyFromValue(getSymbols(), action);
        } catch (Exception ex) {
            throw new ParserException(ex);
        }
    }

    /**
     * Method description
     *
     *
     * @param text
     *
     * @return
     */
    protected String clearBreakLines(String text) {
        return text.replace(LF, "").replace(CR, "");
    }

    /**
     * Method description
     *
     *
     * @param line
     *
     * @return
     */
    protected boolean isChunkLine(String line) {
        Action act = null;

        try {
            act = getAction(line.substring(0, 1));
        } catch (Exception ex) {
            return false;
        }

        return (act != null);
    }

    /**
     * Method description
     *
     *
     * @param line
     *
     * @return
     */
    protected boolean isMissingNewLine(String line) {
        return (!line.isEmpty()) && line.startsWith(NEWLINE_HEADER);
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
    protected String[] getLines(InputStream delta) throws ParserException {
        String sDelta;

        try {
            sDelta = UtilStream.toString(delta);

            return TextHelper.toArray(sDelta, CR);
        } catch (Exception ex) {
            throw new ParserException(ex);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
