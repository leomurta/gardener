package br.uff.ic.gardener.patch.chunk.util;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.chunk.action.Action;

/**
 *
 * @author Daniel
 */
public class AChunk {
    private Action action;

    /**
     *
     */
    protected AChunk() {
        setAction(null);
    }

    /**
     *
     * @param action
     */
    protected AChunk(Action action) {
        setAction(action);
    }

    /**
     *
     * @return
     */
    public Action getAction() {
        return action;
    }

    /**
     *
     * @param action
     */
    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    protected Object clone() {
        return new AChunk(this.getAction());
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
