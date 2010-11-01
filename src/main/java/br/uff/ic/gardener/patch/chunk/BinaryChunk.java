package br.uff.ic.gardener.patch.chunk;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.parser.BinaryParser;

/**
 *
 * @author Daniel
 */
public class BinaryChunk extends BasicChunk implements Chunk {
    private byte[] storedData;

    /**
     *
     * @param action
     * @param storedData
     */
    public BinaryChunk(Action action, byte[] storedData) {
        super(action);
        setStoredData(storedData);
    }

    /**
     * @return the storedData
     */
    public final byte[] getStoredData() {
        return storedData;
    }

    /**
     * @param storedData the storedData to set
     */
    public final void setStoredData(byte[] storedData) {
        this.storedData = storedData;
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();

        text.append(toString(getAction(), new BinaryParser()));
        text.append(" ");
        text.append(getStoredData());
        text.append("\n");

        return text.toString();
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
