package br.uff.ic.gardener.patch.parser;

import br.uff.ic.gardener.patch.chunk.Chunk;
import br.uff.ic.gardener.patch.chunk.Chunk.Action;
import br.uff.ic.gardener.patch.chunk.ContextChunk;
import br.uff.ic.gardener.patch.delta.ContextDelta;
import br.uff.ic.gardener.patch.delta.Delta;
import br.uff.ic.gardener.patch.delta.FileInfo;
import br.uff.ic.gardener.patch.deltaitem.ContextDeltaItem;
import br.uff.ic.gardener.patch.deltaitem.DeltaItemInfo;
import br.uff.ic.gardener.util.TextHelper;
import br.uff.ic.gardener.util.UtilStream;

import java.io.InputStream;

import java.util.LinkedList;

/**
 *
 * @author Daniel
 */
public class ContextParser extends BasicParser implements Parser {

    /** Field description */
    private static final String DELTAITEM_HEADER = "***************";

    /** Field description */
    public static final String NEW_IDENT = "---";

    /** Field description */
    private static final int NEW_INDEX = 2;

    /** Field description */
    public static final String NEW_SYMBOL = "-";

    /** Field description */
    public static final String ORIGINAL_IDENT = "***";

    /** Field description */
    private static final int ORIGINAL_INDEX = 1;

    /** Field description */
    public static final String ORIGINAL_SYMBOL = "*";

    /**
     * Method description
     *
     */
    @Override
    protected void setupSymbols() {
        super.setupSymbols();
        addSymbol("+", Action.ADDED);
        addSymbol("-", Action.DELETED);
        addSymbol("!", Action.MODIFIED);
        addSymbol(" ", Action.CONTEXT);
        addSymbol("m", Action.MOVED);    // not used
    }

    /**
     * Method description
     *
     *
     * @param delta
     *
     * @return
     *
     * @throws Exception
     */
    @Override
    public LinkedList<Result> parseDeltas(InputStream delta) throws Exception {
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
     * @throws Exception
     */
    @Override
    public Delta parseDelta(InputStream delta) throws Exception {
        String       sDelta    = UtilStream.toString(delta);
        final String separator = "\n";
        String[]     lines     = TextHelper.toArray(sDelta, separator);

        if (lines.length < 2) {
            throw new Exception("Invalid patch: header incomplete");
        }

        int currentLine = 0;    // current line

        // jump first lines, headers
        while (!isInfoLine(lines, currentLine)) {
            currentLine++;
        }

        // Infos
        ContextDelta newDelta = new ContextDelta();

        currentLine = setInfo(lines, currentLine, newDelta, ORIGINAL_INDEX);
        currentLine = setInfo(lines, currentLine, newDelta, NEW_INDEX);

        ContextDeltaItem item          = null;
        boolean          originalChunk = true;

        for (int i = currentLine; i < lines.length; i++) {
            String line = clearBreakLines(lines[i]);

            if (line.isEmpty()) {
                continue;
            } else if (isDeltaItemLine(line)) {

                // store previous
                if (item != null) {
                    newDelta.addDeltaItens(item);
                }

                // create new
                item = new ContextDeltaItem(null, null, null);
            } else if (isDeltaItemInfoLine(line)) {
                originalChunk = isOriginalDeltaItemLine(line);
                setupDeltaItemInfo(item, line, originalChunk);
            } else if (isChunkLine(line)) {
                addChunk(item, line, originalChunk);
            } else if (!isMissingNewLine(line)) {
                throw new Exception("Parsing error");
            }
        }

        // store last one
        if (item != null) {
            newDelta.addDeltaItens(item);
        }

        return newDelta;
    }

    /**
     * Method description
     *
     *
     * @param line
     *
     * @return
     */
    private boolean isDeltaItemInfoLine(String line) {
        if (line.isEmpty()) {
            return false;
        }

        return line.startsWith(NEW_IDENT) || isOriginalDeltaItemLine(line);
    }

    /**
     * Method description
     *
     *
     * @param line
     *
     * @return
     */
    private boolean isOriginalDeltaItemLine(String line) {
        if (line.isEmpty()) {
            return false;
        }

        return line.startsWith(ORIGINAL_IDENT);
    }

    /**
     * Method description
     *
     *
     * @param lines
     * @param currentLine
     *
     * @return
     *
     * @throws Exception
     */
    private boolean isInfoLine(String[] lines, int currentLine) throws Exception {
        validateLine(lines, currentLine);

        return isDeltaItemInfoLine(lines[currentLine]);
    }

    /**
     * Method description
     *
     *
     * @param lines
     * @param currentLine
     *
     * @throws Exception
     */
    private void validateLine(String[] lines, int currentLine) throws Exception {
        if (currentLine >= lines.length) {
            throw new Exception("Invalid line");
        }
    }

    /**
     *
     * @param lines
     * @param curLine
     * @param delta
     * @param infoIndex
     * @return
     * @throws Exception
     */
    private int setInfo(String[] lines, int curLine, Delta delta, int infoIndex) throws Exception {
        String buffer;

        // verifies line
        validateLine(lines, curLine);

        String tokens[] = lines[curLine].split("\t");

        if (tokens.length != 2) {
            throw new Exception("Invalid line");
        }

        FileInfo info = new FileInfo();

        // Path
        // remove ident from line begining
        if (infoIndex == ORIGINAL_INDEX) {
            buffer = tokens[0].replace(ORIGINAL_IDENT, "");
        } else {
            buffer = tokens[0].replace(NEW_IDENT, "");
        }

        // Remove blanks
        buffer = buffer.trim();
        info.setPath(buffer);

        // Date
        buffer = tokens[1].trim();
        info.setDate(buffer);

        if (infoIndex == ORIGINAL_INDEX) {
            delta.setOriginalFileInfo(info);
        } else {
            delta.setNewFileInfo(info);
        }

        return curLine + 1;
    }

    /**
     * Method description
     *
     *
     * @param item
     * @param line
     * @param original
     *
     * @throws Exception
     */
    private void addChunk(ContextDeltaItem item, String line, boolean original) throws Exception {
        if (item == null) {
            throw new Exception("Implementation error");
        }

        Chunk.Action action = getAction(line.substring(0, 1));
        String       sText  = "";

        if (line.length() > 1) {
            sText = line.substring(2);
        }

        item.addChunk(new ContextChunk(action, sText, original));
    }

    /**
     * Method description
     *
     *
     * @param item
     * @param line
     * @param originalChunk
     *
     * @throws Exception
     */
    private void setupDeltaItemInfo(ContextDeltaItem item, String line, boolean originalChunk) throws Exception {
        String replace = (originalChunk)
                         ? ORIGINAL_SYMBOL
                         : NEW_SYMBOL;

        // Remove markers and blanks
        line = line.replace(replace, "").trim();

        String infos[] = TextHelper.toArray(line, ",");
        int    start   = 0;
        int    lenght  = 0;

        if (infos.length == 2) {
            start  = Integer.parseInt(infos[0]);
            lenght = Integer.parseInt(infos[1]);
        } else if (infos.length == 1) {
            start = Integer.parseInt(infos[0]);
        } else {
            throw new Exception("Parsing error");
        }

        if (originalChunk) {
            item.setOriginalFileInfo(new DeltaItemInfo(start, lenght));
        } else {
            item.setNewFileInfo(new DeltaItemInfo(start, lenght));
        }
    }

    /**
     * Method description
     *
     *
     * @param line
     *
     * @return
     */
    private boolean isDeltaItemLine(String line) {
        return line.trim().contains(DELTAITEM_HEADER);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
