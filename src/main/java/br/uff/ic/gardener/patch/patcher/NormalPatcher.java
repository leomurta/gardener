package br.uff.ic.gardener.patch.patcher;

import br.uff.ic.gardener.patch.Patch.Match;
import br.uff.ic.gardener.patch.chunk.Chunk;
import br.uff.ic.gardener.patch.chunk.NormalChunk;
import br.uff.ic.gardener.patch.delta.Delta;
import br.uff.ic.gardener.patch.deltaitem.DeltaItem;
import br.uff.ic.gardener.patch.deltaitem.NormalDeltaItem;
import br.uff.ic.gardener.patch.parser.Result;

import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class NormalPatcher extends BasicPatcher implements Patcher {

    /**
     *
     *
     * @param input
     * @param results
     * @return
     *
     * @throws PatcherException
     */
    @Override
    public OutputStream patch( InputStream input, LinkedList<Result> results ) throws PatcherException {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    /**
     * Method description
     *
     *
     * @param input
     * @param patch
     * @param match
     *
     * @return
     *
     * @throws PatcherException
     */
    @Override
    public OutputStream patch( InputStream input, InputStream patch, Match match ) throws PatcherException {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    /**
     * Method description
     *
     *
     * @param input
     * @param delta
     * @param match
     *
     * @return
     *
     * @throws PatcherException
     */
    @Override
    public OutputStream patch( InputStream input, Delta delta, Match match ) throws PatcherException {
        setup( input, delta, match );

        // Convert input to text
        LinkedList<String> text = getLines( input );

        // Results of delta aplications
        List<ApplyDeltaItemResult> results = new ArrayList<ApplyDeltaItemResult>();

        // Applying delta items
        int displacement = 0;    // displacement on line positions

        for (DeltaItem item : delta.getDeltaItens()) {
            ApplyDeltaItemResult result = new ApplyDeltaItemResult( item );

            try {
                displacement = applyChunk( (NormalDeltaItem) item, text, displacement );
                result.setResult( true );
            } catch (Exception e) {
                result.setResult( false );
            }

            // adding result info
            results.add( result );
        }

        // store patch result
        setLastApplyResults( results );

        return toOutpuStream( text );
    }

    /**
     * Method description
     *
     *
     * @param item
     * @param text
     * @param displacement
     *
     *
     * @return
     * @throws PatcherException
     */
    private int applyChunk( NormalDeltaItem item, LinkedList<String> text, int displacement ) throws PatcherException {

        // Corrects for 0 based
        int index = displacement - 1;

        // Corrects index of add operation
        if (item.isAddOperation()) {
            index += item.getNewFileInfo().getStart();

            // new line
            if (item.getNewFileInfo().getStart() == item.getOriginalFileInfo().getStart()) {
                index++;
            }
        } else {
            index += item.getOriginalFileInfo().getStart();
        }

        if (index < 0) {
            throw new PatcherException( PatcherException.MSG_MATCHERROR );
        }

        // Applying chunks
        for (Chunk bChunk : item.getChunks()) {

            // Verifies instances
            if (!(bChunk instanceof NormalChunk)) {
                throw new PatcherException();
            }

            NormalChunk chunk = (NormalChunk) bChunk;

            // Apply
            if (chunk.isDelete()) {

                // Confirms match
                verifyLineMatch( text, index, chunk.getText() );

                // remove line
                text.remove( index );
                displacement--;
            } else if (chunk.isInsert()) {

                // insert line
                text.add( index, chunk.getText() );
                index++;
                displacement++;
            } else {
                throw new PatcherException( PatcherException.MSG_INVALIDACTION );
            }
        }

        return displacement;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
