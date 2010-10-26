package br.uff.ic.gardener.patch.deltaitem;

/**
 *
 * @author Daniel
 */
public class DeltaItemInfo {
    private int lenght = -1;    // lenght of differences
    private int start  = -1;    // first difference line

    /**
     *
     * @param start
     * @param lenght
     */
    public DeltaItemInfo(int start, int lenght) {
        this.lenght = lenght;
        this.start  = start;
    }

    /**
     * @return the start
     */
    public int getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * @return the lenght
     */
    public int getLenght() {
        return lenght;
    }

    /**
     * @param lenght the lenght to set
     */
    public void setLenght(int lenght) {
        this.lenght = lenght;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
