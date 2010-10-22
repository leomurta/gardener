package br.uff.ic.gardener.patch.chunk;

/**
 *
 * @author Daniel
 */
public class BinaryChunk extends BasicChunk implements Chunk {
    private byte[] storedData;

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

    /**
     *
     * @param action
     * @return
     * @throws Exception
     */
    @Override
    public String toString(Action action) {
        if (action == Action.Add) {
            return "+";
        } else if (action == Action.Del) {
            return "-";
        } else if (action == Action.Mod) {
            return "//";
        } else if (action == Action.Mov) {
            return "m";
        } else if (action == Action.Non) {
            return " ";
        } else {
            return super.toString(action);
        }
    }

    @Override
    public String toString() {
        return super.toString();
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
