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
public interface DeltaItem extends Cloneable {
    public Info getInfo1();

    public void setInfo1(Info info);

    public Info getInfo2();

    public void setInfo2(Info info);

    /**
     * @return the deltas
     */
    public LinkedList<Chunk> getChunks();

    /**
     * @param chunks
     */
    public void setChunks(LinkedList<Chunk> chunks);

    /**
     *
     * @return
     */
    @Override
    public String toString();

    /**
     *
     * @return
     */
    public Object clone();
}


//~ Formatted by Jindent --- http://www.jindent.com
