package br.uff.ic.gardener.patch.patcher;

import br.uff.ic.gardener.patch.Patch.Match;
import br.uff.ic.gardener.patch.chunk.Chunk;
import br.uff.ic.gardener.patch.chunk.UnifiedChunk;
import br.uff.ic.gardener.patch.delta.Delta;
import br.uff.ic.gardener.patch.deltaitem.DeltaItem;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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
     * @param delta
     * @param match
     * @return
     *
     *
     * @throws PatcherException
     */
    @Override
    public OutputStream patch(InputStream input, Delta delta, Match match) throws PatcherException {
        setup(input, match);

        // Convert input to text
        LinkedList<String> text = getLines(input);

        // Results of delta aplications
        List<ApplyDeltaItemResult> results = new ArrayList<ApplyDeltaItemResult>();

        // difference resulting from applying chunks
        int displacement = 0;

        // Applying delta items
        for (DeltaItem item : delta.getDeltaItens()) {
            ApplyDeltaItemResult result = new ApplyDeltaItemResult(item);

            // Choosing matching algoritm
            if (isNoMatch()) {
                displacement = applyNoMatch(item, text, result, displacement);
            } else if (isCompleteMatch()) {
                applyCompleteMatch(item, text, result);
            } else {
                throw new PatcherException(PatcherException.MSG_INVALIDMATCH);
            }

            // adding result info
            results.add(result);
        }

        // store patch result
        setLastApplyResults(results);

        return toOutpuStream(text);
    }

    /**
     * SAX patch
     * @param input
     * @param patch
     * @param match
     * @return
     *
     *
     * @throws PatcherException
     */
    @Override
    public OutputStream patch(InputStream input, InputStream patch, Match match) throws PatcherException {
        throw new UnsupportedOperationException("Not supported yet.");
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
     *
     * @throws PatcherException
     */
    private int applyNoMatch(DeltaItem item, LinkedList<String> text, ApplyDeltaItemResult result, int displacement)
            throws PatcherException {

        // Index is 0 based
        int index = displacement + item.getOriginalFileInfo().getStart() - 1;

        // Chunk start position incorrect
        if (index < 0) {
            throw new PatcherException(PatcherException.MSG_MATCHERROR);
        }

        // Applying chunks
        for (Chunk chunk : item.getChunks()) {
            if (!(chunk instanceof UnifiedChunk)) {
                throw new PatcherException();
            }

            UnifiedChunk uChunk = (UnifiedChunk) chunk;

            // Apply
            if (uChunk.isContext()) {

                // Confirms match
                verifyLineMatch(text, index, uChunk.getText());
                index++;
            } else if (uChunk.isDelete()) {

                // Confirms match
                verifyLineMatch(text, index, uChunk.getText());
                text.remove(index);
                displacement--;
            } else if (uChunk.isInsert()) {
                text.add(index, uChunk.getText());
                index++;
                displacement++;
            } else {
                throw new PatcherException(PatcherException.MSG_INVALIDACTION);
            }
        }

        // Chunks applied successfully
        result.setResult(true);

        return displacement;
    }

    /**
     * Method description
     *
     *
     * @param item
     * @param text
     * @param result
     *
     *
     *
     * @throws PatcherException
     */
    private void applyCompleteMatch(DeltaItem item, LinkedList<String> text, ApplyDeltaItemResult result)
            throws PatcherException {

        // Index is 0 based
        int index = getCompleteMatchLine(item, text);

        // Chunk start position not found, do not apply
        if (index < 0) {
            result.setResult(false);

            return;
        }

        // remove contexto do inicio e fim, pular linha nos demais
        boolean bIgnoreContext = true;

        // Applying chunks
        for (Chunk chunk : item.getChunks()) {
            if (!(chunk instanceof UnifiedChunk)) {
                throw new PatcherException();
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
                verifyLineMatch(text, index, uChunk.getText());
                text.remove(index);

                // displacement--;
            } else if (uChunk.isInsert()) {
                bIgnoreContext = false;    // next context won´t be ignored
                text.add(index, uChunk.getText());
                index++;

                // displacement++;
            } else {
                throw new PatcherException(PatcherException.MSG_INVALIDACTION);
            }
        }

        // Chunks applied successfully
        result.setResult(true);
    }

    /**
     * Method description
     *
     *
     *
     * @param item
     * @param text
     *
     * @return
     *
     *
     * @throws PatcherException
     */
    private int getCompleteMatchLine(DeltaItem item, LinkedList<String> text) throws PatcherException {

        // Corrects for 0 based
        int startLine = item.getOriginalFileInfo().getStart() - 1;

        if (startLine < 0) {
            throw new PatcherException(PatcherException.MSG_MATCHERROR);
        }

        // context of alterations
        LinkedList<String> context = new LinkedList<String>();

        // Getter context lines
        for (Chunk chunk : item.getChunks()) {
            if (!(chunk instanceof UnifiedChunk)) {
                throw new PatcherException();
            }

            UnifiedChunk uChunk = (UnifiedChunk) chunk;

            if (uChunk.isContext()) {
                context.add(uChunk.getText());
            } else {
                break;
            }
        }

        return Matcher.match(startLine, context, text);
    }
}
