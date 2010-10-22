package br.uff.ic.gardener.patch.chunk;

/**
 *
 * @author Daniel
 */
public interface Chunk extends Cloneable {

    /**
     *
     */
    public enum Action {
        Add, Del, Mod, Mov, Non
    }

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
     * @param action
     * @return
     * @throws Exception
     */
    public String toString(Action action);

    /**
     *
     * @param action
     * @return
     * @throws Exception
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
