package br.uff.ic.gardener.patch.deltaitem;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.delta.*;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;

/**
 *
 * @author Daniel
 */
public class Info {
    private int lenght = -1;    // lenght of differences
    private int start  = -1;    // first difference line

    /**
     *
     */
    public Info(int start, int lenght) {
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
