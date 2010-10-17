
package br.uff.ic.gardener.patch.chunk;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.chunk.action.Action;

/**
 *
 * @author Daniel
 */
public interface Chunk extends Cloneable {
    /**
     * 
     * @return
     */
    public Action getAction();

    /**
     *
     * @param action
     */
    public void setAction(Action action);

    /**
     *
     * @return
     */
    public Object clone();
}


//~ Formatted by Jindent --- http://www.jindent.com
