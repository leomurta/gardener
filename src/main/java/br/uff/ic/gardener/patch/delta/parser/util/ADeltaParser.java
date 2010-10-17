package br.uff.ic.gardener.patch.delta.parser.util;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.chunk.Chunk;
import br.uff.ic.gardener.patch.chunk.action.Action;
import br.uff.ic.gardener.patch.delta.Delta;

//~--- JDK imports ------------------------------------------------------------

import java.util.LinkedList;

/**
 *
 * @author Daniel
 */
public class ADeltaParser {

    /**
     *
     * @param deltas
     * @return
     * @throws Exception
     */
    public String toString(LinkedList<Delta> deltas) throws Exception {
        StringBuilder text = new StringBuilder();

        boolean bFirst = true;
        for (Delta aDelta : deltas) {

            if(bFirst){
                headerToString(aDelta);
                bFirst = false;
            }

            text.append(toString(aDelta));
            text.append("\n");
        }

        return text.toString();
    }

    /**
     *
     * @param delta
     * @return
     * @throws Exception
     */
    public String toString(Delta delta) throws Exception {
        StringBuilder text = new StringBuilder();

        // Header
        text.append(deltaToString(delta));

        // Chunks
        for (Chunk aChunk : delta.getChunks()) {
            text.append(chunkToString(aChunk));
        }

        return text.toString();
    }

    /**
     * Should be overloaded
     * @param delta
     * @return
     * @throws Exception
     */
    protected String deltaToString(Delta delta) throws Exception {
        throw new Exception("Implementation error. Method should be overloaded");
    }

    /**
     * Should be overloaded
     * @param delta
     * @return
     * @throws Exception
     */
    protected String headerToString(Delta delta) throws Exception {
        throw new Exception("Implementation error. Method should be overloaded");
    }

    /**
     * Default
     * @param aChunk
     * @return
     * @throws Exception
     */
    protected String chunkToString(Chunk aChunk) throws Exception {
        throw new Exception("Implementation error. Method should be overloaded");
    }

    /**
     *
     * @param action
     * @return
     * @throws Exception
     */
    protected String actionToString(Action action) throws Exception {
        throw new Exception("Unsupported action instance:" + action.getClass());
    }

    /**
     *
     * @param chunk
     * @return
     * @throws Exception
     */
    protected String chunkDataToString(Chunk chunk) throws Exception {
        throw new Exception("Unsupported chunk instance:" + chunk.getClass());
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
