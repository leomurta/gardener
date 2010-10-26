package br.uff.ic.gardener.patch.deltaitem;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.chunk.Chunk;
import br.uff.ic.gardener.patch.delta.*;

//~--- JDK imports ------------------------------------------------------------

import java.util.LinkedList;

/**
 *
 * @author Daniel
 */
public class BasicDeltaItem {
    private LinkedList<Chunk> chunks;
    private DeltaItemInfo     newFileInfo;
    private DeltaItemInfo     originalFileInfo;

    /**
     *
     * @param info1
     * @param info2
     * @param chunks
     */
    protected BasicDeltaItem(DeltaItemInfo info1, DeltaItemInfo info2, LinkedList<Chunk> chunks) {
        setChunks(chunks);
        setOriginalFileInfo(info1);
        setNewFileInfo(info2);
    }

    /**
     *
     * @return
     */
    public DeltaItemInfo getOriginalFileInfo() {
        return this.originalFileInfo;
    }

    /**
     *
     * @param info
     */
    public final void setOriginalFileInfo(DeltaItemInfo info) {
        this.originalFileInfo = info;
    }

    /**
     *
     * @return
     */
    public DeltaItemInfo getNewFileInfo() {
        return this.newFileInfo;
    }

    /**
     *
     * @param info
     */
    public final void setNewFileInfo(DeltaItemInfo info) {
        this.newFileInfo = info;
    }

    /**
     *
     * @return
     */
    public final LinkedList<Chunk> getChunks() {
        return chunks;
    }

    /**
     *
     * @param chunks
     */
    public final void setChunks(LinkedList<Chunk> chunks) {
        this.chunks = chunks;
    }

    public final void addChunk(Chunk chunk) {
        if (getChunks() == null) {
            setChunks(new LinkedList<Chunk>());
        }

        getChunks().add(chunk);
    }

    @Override
    public Object clone() {
        return new BasicDeltaItem(this.getOriginalFileInfo(), this.getNewFileInfo(), this.getChunks());
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
