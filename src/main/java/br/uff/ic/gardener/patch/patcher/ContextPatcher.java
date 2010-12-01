package br.uff.ic.gardener.patch.patcher;

import br.uff.ic.gardener.patch.Patch.Match;
import br.uff.ic.gardener.patch.chunk.Chunk;
import br.uff.ic.gardener.patch.chunk.ContextChunk;
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
public class ContextPatcher extends BasicPatcher implements Patcher {

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
     * @param input
     * @param delta
     * @param match
     *
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

        // Applying delta items
        for (DeltaItem item : delta.getDeltaItens()) {
            ApplyDeltaItemResult result = new ApplyDeltaItemResult(item);

                try {
                    applyCompleteMatch(item, text);
                    result.setResult(true);
                } catch (Exception e) {
                    result.setResult(false);
            }

            // adding result info
            results.add(result);
        }

        // store patch result
        setLastApplyResults(results);

        return toOutpuStream(text);
    }

    /**
     * 
     * @param input
     * @param match
     * @throws PatcherException
     */
    @Override
    protected void setup(InputStream input, Match match) throws PatcherException {
        super.setup(input, match);

        if (isNoMatch()) {
            throw new PatcherException(PatcherException.MSG_INVALIDMATCH);
        }
    }

    /**
     * Method description
     *
     *
     * @param item
     * @param text
     *
     *
     *
     * @throws PatcherException
     */
    private void applyCompleteMatch(DeltaItem item, LinkedList<String> text) throws PatcherException {

        // Corrects for 0 based
        int index = item.getOriginalFileInfo().getStart() - 1;

        if (index < 0) {
            throw new PatcherException(PatcherException.MSG_MATCHERROR);
        }

        // context of alterations
        LinkedList<String> context = new LinkedList<String>();
        boolean readContext = false;
        boolean foundFirstNewChunk = false;

        // Applying chunks
        for (Chunk bChunk : item.getChunks()) {

            // Verifies instances
            if (!(bChunk instanceof ContextChunk)) {
                throw new PatcherException();
            }

            ContextChunk chunk = (ContextChunk) bChunk;

            // clean context before next blocks
            if (!chunk.isOriginal() && !foundFirstNewChunk) {
                foundFirstNewChunk = true;
                context.clear();
            }

            // Apply
            if (chunk.isContext()) {
                readContext = true;
                context.add(chunk.getText());
            } else if (chunk.isInsert()) {
                if (readContext) {
                    index = getCompleteMatchLine(context, index, text);
                    context.clear();
                    readContext = false;
                }

                text.add(index, chunk.getText());
                index++;
            } else if (chunk.isDelete()) {
                if (readContext) {
                    index = getCompleteMatchLine(context, index, text);
                    context.clear();
                    readContext = false;
                }

                // Confirms match
                verifyLineMatch(text, index, chunk.getText());
                text.remove(index);
            } else if (chunk.isModified() && (!chunk.isOriginal())) {
                if (readContext) {
                    index = getCompleteMatchLine(context, index, text);
                    context.clear();
                    readContext = false;
                }

                text.add(index, chunk.getText());
                index++;
            } else if (chunk.isModified() && chunk.isOriginal()) {
                if (readContext) {
                    index = getCompleteMatchLine(context, index, text);
                    context.clear();
                    readContext = false;
                }

                // Confirms match
                verifyLineMatch(text, index, chunk.getText());
                text.remove(index);
            } else {
                throw new PatcherException(PatcherException.MSG_INVALIDACTION);
            }
        }
    }

    /**
     * Method description
     *
     *
     *
     *
     * @param context
     * @param index
     * @param text
     *
     * @return
     *
     *
     * @throws PatcherException
     */
    private int getCompleteMatchLine(LinkedList<String> context, int index, LinkedList<String> text)
            throws PatcherException {
        index = Matcher.match(index, context, text);

        if (index < 0) {
            throw new PatcherException(PatcherException.MSG_MATCHERROR);
        }

        return index;
    }
}
