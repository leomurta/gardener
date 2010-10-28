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

        /**
         *
         */
        ADDED,

        /**
         *
         */
        DELETED,

        /**
         *
         */
        MODIFIED,

        /**
         *
         */
        MOVED,

        /**
         *
         */
        CONTEXT
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

    public boolean isInsert();

    public boolean isDelete();

    public boolean isContext();

    /**
     *
     * @return
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
