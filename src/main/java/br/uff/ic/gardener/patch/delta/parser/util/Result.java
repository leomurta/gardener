package br.uff.ic.gardener.patch.delta.parser.util;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.delta.Delta;

//~--- JDK imports ------------------------------------------------------------

import java.io.FileInputStream;

/**
 * 
 * @author Daniel
 */
public class Result {
    private Delta           delta;
    private FileInputStream stream;

    /**
     * Constructor
     * @param delta
     * @param stream
     */
    Result(Delta delta, FileInputStream stream) {
        setDelta(delta);
        setStream(stream);
    }

    /**
     * @return the delta
     */
    public Delta getDelta() {
        return delta;
    }

    /**
     * @param delta the delta to set
     */
    public void setDelta(Delta delta) {
        this.delta = delta;
    }

    /**
     * @return the stream
     */
    public FileInputStream getStream() {
        return stream;
    }

    /**
     * @param stream the stream to set
     */
    public void setStream(FileInputStream stream) {
        this.stream = stream;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
