
package br.uff.ic.gardener.patch.chunk;

import br.uff.ic.gardener.patch.chunk.util.AChunk;

/**
 *
 * @author Daniel
 */
public class BinaryChunk extends AChunk implements Chunk {
    private byte[] storedData;

    /**
     * @return the storedData
     */
    public byte[] getStoredData() {
        return storedData;
    }

    /**
     * @param storedData the storedData to set
     */
    public void setStoredData(byte[] storedData) {
        this.storedData = storedData;
    }

    /**
     *
     * @return
     */
    @Override
    public Object clone() {
        BinaryChunk clone = (BinaryChunk) super.clone();

        clone.setStoredData(this.getStoredData());

        return clone;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
