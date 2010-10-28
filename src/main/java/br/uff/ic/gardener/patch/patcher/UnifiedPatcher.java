package br.uff.ic.gardener.patch.patcher;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.Patch.Match;
import br.uff.ic.gardener.patch.chunk.Chunk;
import br.uff.ic.gardener.patch.chunk.UnifiedChunk;
import br.uff.ic.gardener.patch.delta.Delta;
import br.uff.ic.gardener.patch.deltaitem.DeltaItem;
import br.uff.ic.gardener.patch.parser.Result;
import br.uff.ic.gardener.util.TextHelper;
import br.uff.ic.gardener.util.UtilStream;

//~--- JDK imports ------------------------------------------------------------

import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class UnifiedPatcher extends BasicPatcher implements Patcher {

    /**
     * OO patch
     *
     * @param input
     * @param results
     * @return
     *
     * @throws Exception
     */
    @Override
    public OutputStream patch( InputStream input, LinkedList<Result> results ) throws Exception {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    /**
     * OO patch
     *
     * @param input
     * @param delta
     * @param match
     * @return
     *
     * @throws Exception
     */
    @Override
    public OutputStream patch( InputStream input, Delta delta, Match match ) throws Exception {
        setup( input, delta, match );

        // Convert input to text
        String             sText = UtilStream.toString( input );
        LinkedList<String> text  = new LinkedList<String>();

        text.addAll( Arrays.asList( TextHelper.toArray( sText ) ) );

        // Results of delta aplications
        List<ApplyDeltaItemResult> results = new ArrayList<ApplyDeltaItemResult>();

        // difference resulting from applying chunks
        int displacement = 0;

        // Applying delta items
        for (DeltaItem item : delta.getDeltaItens()) {
            ApplyDeltaItemResult result = new ApplyDeltaItemResult( item );

            // Choosing matching algoritm
            if (isNoMatch()) {
                displacement = applyNoMatch( item, text, result, displacement );
            } else if (isCompleteMatch()) {
                displacement = applyCompleteMatch( item, text, result, displacement );
            } else {
                throw new Exception( "Unsupported matching type" );
            }

            // adding result info
            results.add( result );
        }

        // store patch result
        setLastApplyResults( results );

        return toOutpuStream( text );
    }

    /**
     * SAX patch
     * @param input
     * @param patch
     * @param match
     * @return
     *
     * @throws Exception
     */
    @Override
    public OutputStream patch( InputStream input, InputStream patch, Match match ) throws Exception {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    /**
     * Method description
     *
     *
     * @param item
     * @param text
     * @param result
     * @param displacement
     *
     * @return
     *
     * @throws Exception
     */
    private int applyNoMatch( DeltaItem item, LinkedList<String> text, ApplyDeltaItemResult result, int displacement )
            throws Exception {

        // Index is 0 based
        int index = displacement + item.getOriginalFileInfo().getStart() - 1;

        // Chunk start position incorrect
        if (index < 0) {
            throw new Exception( "Patching error: invalid line index" );
        }

        // Applying chunks
        for (Chunk chunk : item.getChunks()) {
            if (!(chunk instanceof UnifiedChunk)) {
                throw new Exception( "Patching error: invalid instance" );
            }

            UnifiedChunk uChunk = (UnifiedChunk) chunk;

            // Apply
            if (uChunk.isContext()) {

                // Confirms match
                if (!isMatchingLine( text.get( index ), uChunk.getText() )) {
                    throw new Exception( "Patching error: invalid test" );
                }

                index++;
            } else if (uChunk.isDelete()) {

                // Confirms match
                if (!isMatchingLine( text.get( index ), uChunk.getText() )) {
                    throw new Exception( "Patching error: invalid test" );
                }

                text.remove( index );
                displacement--;
            } else if (uChunk.isInsert()) {
                text.add( index, uChunk.getText() );
                index++;
                displacement++;
            } else {
                throw new Exception( "Patching error: Unsupported chunk action" );
            }
        }

        // Chunks applied successfully
        result.setResult( true );

        return displacement;
    }

    /**
     * Method description
     *
     *
     * @param item
     * @param text
     * @param result
     * @param displacement
     *
     * @return
     *
     * @throws Exception
     */
    private int applyCompleteMatch( DeltaItem item, LinkedList<String> text, ApplyDeltaItemResult result,
                                    int displacement )
            throws Exception {

        // Index is 0 based
        int index = getCompleteMatchLine( item, text, displacement );

        // Chunk start position not found, do not apply
        if (index < 0) {
            result.setResult( false );

            return displacement;
        }

        // remove contexto do inicio e fim, pular linha nos demais
        boolean bIgnoreContext = true;

        // Applying chunks
        for (Chunk chunk : item.getChunks()) {
            if (!(chunk instanceof UnifiedChunk)) {
                throw new Exception( "Patching error" );
            }

            UnifiedChunk uChunk = (UnifiedChunk) chunk;

            // Apply
            if (uChunk.isContext()) {

                // Ignore inicial and final context
                if (bIgnoreContext) {
                    continue;
                }

                // Jump lines on other
                index++;
            } else if (uChunk.isDelete()) {
                bIgnoreContext = false;    // next context won´t be ignored

                // Confirms match
                if (!isMatchingLine( text.get( index ), uChunk.getText() )) {
                    throw new Exception( "Matching error" );
                }

                text.remove( index );

                // displacement--;
            } else if (uChunk.isInsert()) {
                bIgnoreContext = false;    // next context won´t be ignored
                text.add( index, uChunk.getText() );
                index++;

                // displacement++;
            } else {
                throw new Exception( "Unsupported chunk action" );
            }
        }

        // Chunks applied successfully
        result.setResult( true );

        return displacement;
    }

    /**
     * Method description
     *
     *
     *
     * @param item
     * @param text
     * @param displacement
     *
     * @return
     *
     * @throws Exception
     */
    private int getCompleteMatchLine( DeltaItem item, LinkedList<String> text, int displacement ) throws Exception {

        // Add displacement from previous alterations, corrects for 0 based
        int startLine = item.getOriginalFileInfo().getStart() - 1    /* + displacement */
        ;

        if (startLine < 0) {
            throw new Exception( "Matching error" );
        }

        // context of alterations
        LinkedList<String> context = new LinkedList<String>();

        // Getter context lines
        for (Chunk chunk : item.getChunks()) {
            if (!(chunk instanceof UnifiedChunk)) {
                throw new Exception( "Patching error" );
            }

            UnifiedChunk uChunk = (UnifiedChunk) chunk;

            if (uChunk.isContext()) {
                context.add( uChunk.getText() );
            } else {
                break;
            }
        }

        // No context, so no match to find
        if (context.isEmpty()) {

            // Beginning of file
            return startLine;
        }

        // Matching down first
        for (int i = startLine; i < text.size(); i++) {
            if (isMatchingBlock( i, context, text )) {
                return (i + context.size());
            }
        }

        // Matching up
        for (int i = 0; i < startLine; i++) {
            if (isMatchingBlock( i, context, text )) {
                return (i + context.size());
            }
        }

        // No match found
        return -1;
    }

    /**
     * Method description
     *
     *
     * @param index
     * @param context
     * @param text
     *
     * @return
     */
    private boolean isMatchingBlock( int index, LinkedList<String> context, LinkedList<String> text ) {
        for (int i = 0; i < context.size(); i++) {
            String contextLine = context.get( i );
            String textLine    = text.get( index + i );

            // difference found
            if (!isMatchingLine( contextLine, textLine )) {
                return false;
            }
        }

        return true;
    }

    /**
     * Method description
     *
     *
     * @param text1
     * @param text2
     *
     * @return
     */
    private boolean isMatchingLine( String text1, String text2 ) {
        if ((text1 == null) || (text2 == null)) {
            return false;
        }

        text1 = text1.replaceAll( "\r", "" ).replace( "\n", "" );
        text2 = text2.replaceAll( "\r", "" ).replace( "\n", "" );

        return (text2.compareTo( text1 ) == 0);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
