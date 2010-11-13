package br.uff.ic.gardener.patch.parser;

import br.uff.ic.gardener.patch.chunk.Chunk;
import br.uff.ic.gardener.patch.chunk.Chunk.Action;
import br.uff.ic.gardener.patch.chunk.UnifiedChunk;
import br.uff.ic.gardener.patch.delta.Delta;
import br.uff.ic.gardener.patch.delta.FileInfo;
import br.uff.ic.gardener.patch.delta.UnifiedDelta;
import br.uff.ic.gardener.patch.deltaitem.DeltaItemInfo;
import br.uff.ic.gardener.patch.deltaitem.UnifiedDeltaItem;
import br.uff.ic.gardener.util.TextHelper;

import java.io.InputStream;

import java.util.LinkedList;

/**
 *
 * @author Daniel
 */
public class UnifiedParser extends BasicParser implements Parser {

    /** Field description */
    private static final String DELTAITEM_HEADER = "@@";

    /** Field description */
    public static final String NEW_IDENT = "+++";

    /** Field description */
    private static final int NEW_INDEX = 2;

    /** Field description */
    public static final String NEW_SYMBOL = "+";

    /** Field description */
    public static final String ORIGINAL_IDENT = "---";

    /** Field description */
    private static final int ORIGINAL_INDEX = 1;

    /** Field description */
    public static final String ORIGINAL_SYMBOL = "-";

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

    /**
     * Method description
     *
     *
     * @param delta
     *
     * @return
     *
     *
     * @throws ParserException
     */
    @Override
    public LinkedList<Result> parseDeltas(InputStream delta) throws ParserException {
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
     *
     * @throws ParserException
     */
    @Override
    public Delta parseDelta(InputStream delta) throws ParserException {
        String[] lines = getLines(delta);

        if (lines.length < 2) {
            throw new ParserException(ParserException.MSG_INVALIDPATCHHEADER);
        }

        int currentLine = 0;    // current line

        // jump first lines
        while (!isInfoLine(lines, currentLine)) {
            currentLine++;
        }

        UnifiedDelta newDelta = new UnifiedDelta();

        currentLine = setInfo(lines, currentLine, newDelta, ORIGINAL_INDEX);
        currentLine = setInfo(lines, currentLine, newDelta, NEW_INDEX);

        UnifiedDeltaItem item = null;

        for (int i = currentLine; i < lines.length; i++) {
            String line = clearBreakLines(lines[i]);

            if (line.isEmpty()) {
                continue;
            }

            if (isDeltaItemLine(line)) {

                // store previous
                if (item != null) {
                    newDelta.addDeltaItens(item);
                    item = null;
                }

                // creates initial item
                item = setupDeltaItem(line);
            } else if (isChunkLine(line)) {
                addChunk(item, line);
            } else if (!isMissingNewLine(line)) {
                throw new ParserException(ParserException.MSG_INVALIDLINE);
            }
        }    // for

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
     * @param lines
     * @param currentLine
     *
     * @return
     *
     *
     * @throws ParserException
     */
    private boolean isInfoLine(String[] lines, int currentLine) throws ParserException {
        testLine(lines, currentLine);

        String line = lines[currentLine];

        if (line.isEmpty()) {
            return false;
        }

        return line.startsWith(NEW_IDENT) || line.startsWith(ORIGINAL_IDENT);
    }

    /**
     * Method description
     *
     *
     * @param lines
     * @param currentLine
     *
     *
     * @throws ParserException
     */
    private void testLine(String[] lines, int currentLine) throws ParserException {
        if (currentLine >= lines.length) {
            throw new ParserException(ParserException.MSG_INVALIDLINE);
        }
    }

    /**
     *
     * @param lines
     * @param curLine
     * @param delta
     * @param infoIndex
     * @return
     *
     * @throws ParserException
     */
    private int setInfo(String[] lines, int curLine, Delta delta, int infoIndex) throws ParserException {
        String buffer;

        // verifies line
        testLine(lines, curLine);

        String tokens[] = lines[curLine].split("\t");

        if (tokens.length != 2) {
            throw new ParserException(ParserException.MSG_INVALIDLINE);
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
     *
     *
     * @throws ParserException
     */
    private void addChunk(UnifiedDeltaItem item, String line) throws ParserException {
        Chunk.Action action = getAction(line.substring(0, 1));
        String       sText  = line.substring(1);
        UnifiedChunk chunk  = new UnifiedChunk(action, sText);

        item.addChunk(chunk);
    }

    /**
     * Method description
     *
     *
     * @param line
     *
     * @return
     *
     *
     * @throws ParserException
     */
    private UnifiedDeltaItem setupDeltaItem(String line) throws ParserException {
        UnifiedDeltaItem item = new UnifiedDeltaItem(null, null, null);

        // Remove markers and blanks
        String buffer  = line.replace(DELTAITEM_HEADER, "").trim();
        String infos[] = TextHelper.toArray(buffer, " ");

        if (infos.length != 2) {
            throw new ParserException();
        }

        DeltaItemInfo info1 = getInfo(infos[0]);

        item.setOriginalFileInfo(info1);

        DeltaItemInfo info2 = getInfo(infos[1]);

        item.setNewFileInfo(info2);

        return item;
    }

    /**
     * Method description
     *
     *
     * @param text
     *
     * @return
     *
     *
     * @throws ParserException
     */
    private DeltaItemInfo getInfo(String text) throws ParserException {
        String infos[] = TextHelper.toArray(text, ",");

        if (infos.length != 2) {
            throw new ParserException();
        }

        // remove symbols + e -
        infos[0] = infos[0].replace(ORIGINAL_SYMBOL, "").replace(NEW_SYMBOL, "");

        int start  = Integer.parseInt(infos[0]);
        int lenght = Integer.parseInt(infos[1]);

        return new DeltaItemInfo(start, lenght);
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
        line = line.trim();    // remove line breaks and blanks

        boolean b = line.startsWith(DELTAITEM_HEADER);

        b &= line.endsWith(DELTAITEM_HEADER);

        return b;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
