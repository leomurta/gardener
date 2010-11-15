package br.uff.ic.gardener.patch.parser;

import br.uff.ic.gardener.patch.chunk.Chunk;
import br.uff.ic.gardener.patch.chunk.Chunk.Action;
import br.uff.ic.gardener.patch.chunk.NormalChunk;
import br.uff.ic.gardener.patch.delta.Delta;
import br.uff.ic.gardener.patch.delta.NormalDelta;
import br.uff.ic.gardener.patch.deltaitem.DeltaItemInfo;
import br.uff.ic.gardener.patch.deltaitem.NormalDeltaItem;
import br.uff.ic.gardener.util.TextHelper;

import java.io.InputStream;

import java.util.LinkedList;

/**
 *
 * @author Daniel
 */
public class NormalParser extends BasicParser implements Parser {

    /** Field description */
    protected static String OP_ADD = "a";

    /** Field description */
    protected static String OP_CHANGE = "c";

    /** Field description */
    protected static String OP_DELETE = "d";

    /** Field description */
    private static final String SB_ADD = ">";

    /** Field description */
    public static final String SB_DEL = "<";

    /** Field description */
    public static final String SB_SPLIT = "---";

    /**
     *
     * @param deltas
     * @return
     *
     * @throws ParserException
     */
    @Override
    public LinkedList<Result> parseDeltas( InputStream deltas ) throws ParserException {
        throw new UnsupportedOperationException( "Not supported yet." );
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
    public Delta parseDelta( InputStream delta ) throws ParserException {
        String[] lines       = getLines( delta );
        int      currentLine = 0;    // current line

        // Infos
        NormalDelta     newDelta = new NormalDelta();
        NormalDeltaItem item     = null;

        for (int i = currentLine; i < lines.length; i++) {
            String line = clearBreakLines( lines[i] );

            if (line.isEmpty()) {
                continue;
            } else if (isDeltaItemLine( line )) {

                // store previous
                if (item != null) {
                    newDelta.addDeltaItens( item );
                }

                // create new
                item = createDeltaItem( line );
            } else if (isChunkLine( line )) {
                addChunk( item, line );
            } else if (isSeparator( line )) {
                continue;
            } else if (!isMissingNewLine( line )) {
                throw new ParserException( ParserException.MSG_INVALIDLINE );
            }
        }

        // store last one
        if (item != null) {
            newDelta.addDeltaItens( item );
        }

        return newDelta;
    }

    /**
     * Method description
     *
     */
    @Override
    protected void setupSymbols() {
        super.setupSymbols();
        addSymbol( SB_ADD, Action.ADDED );
        addSymbol( SB_DEL, Action.DELETED );
        addSymbol( "", Action.MODIFIED );
        addSymbol( "", Action.MOVED );
        addSymbol( "", Action.CONTEXT );
    }

    /**
     * Method description
     *
     *
     * @param line
     *
     * @return
     */
    private boolean isDeltaItemLine( String line ) {
        return !line.isEmpty() &&!line.startsWith( SB_ADD ) &&!line.startsWith( SB_DEL ) &&!isSeparator( line );
    }

    /**
     * Method description
     *
     *
     * @param line
     *
     * @return
     */
    private boolean isSeparator( String line ) {
        return !line.isEmpty() && (line.compareTo( SB_SPLIT ) == 0);
    }

    /**
     * Method description
     *
     *
     * @param item
     * @param line
     *
     * @throws ParserException
     */
    private void addChunk( NormalDeltaItem item, String line ) throws ParserException {
        Chunk.Action action = getAction( line.substring( 0, 1 ) );
        String       sText  = "";

        if (line.length() > 1) {
            sText = line.substring( 2 );
        }

        item.addChunk( new NormalChunk( action, sText ) );
    }

    /**
     * Method description
     *
     *
     * @param line
     *
     * @return
     *
     * @throws ParserException
     */
    private NormalDeltaItem createDeltaItem( String line ) throws ParserException {
        String op = "";

        if (line.contains( OP_ADD )) {
            op = OP_ADD;
        } else if (line.contains( OP_DELETE )) {
            op = OP_DELETE;
        } else if (line.contains( OP_CHANGE )) {
            op = OP_CHANGE;
        } else {
            throw new ParserException( ParserException.MSG_INVALIDLINE );
        }

        String tokens[] = line.split( op );

        if (tokens.length != 2) {
            throw new ParserException( ParserException.MSG_INVALIDLINE );
        }

        return new NormalDeltaItem( getInfo( tokens[0] ), getInfo( tokens[1] ), null, op );
    }

    /**
     * Method description
     *
     *
     *
     * @param line
     *
     * @return
     *
     *
     * @throws ParserException
     */
    private DeltaItemInfo getInfo( String line ) throws ParserException {
        String infos[] = TextHelper.toArray( line, "," );
        int    start   = 0;
        int    lenght  = 0;

        if (infos.length == 2) {
            start  = Integer.parseInt( infos[0] );
            lenght = Integer.parseInt( infos[1] );
        } else if (infos.length == 1) {
            start = Integer.parseInt( infos[0] );
        } else {
            throw new ParserException();
        }

        return new DeltaItemInfo( start, lenght );
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
