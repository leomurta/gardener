package br.uff.ic.gardener.patch.chunk;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.chunk.Chunk.Action;

/**
 *
 * @author Daniel
 */
public class BasicChunk {
    private Action action;

    /**
     *
     * @param action
     */
    protected BasicChunk(Action action) {
        setAction(action);
    }

    /**
     *
     * @return
     */
    public final Action getAction() {
        return action;
    }

    /**
     *
     * @param action
     */
    public final void setAction(Action action) {
        this.action = action;
    }

    /**
     *
     * @return
     */
    @Override
    protected Object clone() {
        return new BasicChunk(this.getAction());
    }

    /**
     *
     * @param action
     * @throws Exception
     */
    public String toString(Action action) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
