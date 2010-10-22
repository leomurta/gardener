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
    private Info              info1;
    private Info              info2;

    protected BasicDeltaItem(Info info1, Info info2, LinkedList<Chunk> chunks) {
        setChunks(chunks);
        setInfo1(info1);
        setInfo2(info2);
    }

    public Info getInfo1() {
        return this.info1;
    }

    public final void setInfo1(Info info) {
        this.info1 = info;
    }

    public Info getInfo2() {
        return this.info2;
    }

    public final void setInfo2(Info info) {
        this.info2 = info;
    }

    public final LinkedList<Chunk> getChunks() {
        return chunks;
    }

    public final void setChunks(LinkedList<Chunk> chunks) {
        for (Chunk aChunk : chunks) {
            this.chunks.add((Chunk) aChunk.clone());
        }
    }

    @Override
    public Object clone() {
        return new BasicDeltaItem(this.getInfo1(), this.getInfo2(), this.getChunks());
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
