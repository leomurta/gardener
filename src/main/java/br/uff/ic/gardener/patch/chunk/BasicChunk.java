package br.uff.ic.gardener.patch.chunk;

import br.uff.ic.gardener.patch.chunk.Chunk.Action;
import br.uff.ic.gardener.patch.parser.BasicParser;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class BasicChunk {

    /** Field description */
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
     * Method description
     *
     *
     * @return
     */
    public boolean isModified() {
        return (getAction() == Chunk.Action.MODIFIED);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public boolean isInsert() {
        return (getAction() == Chunk.Action.ADDED);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public boolean isDelete() {
        return (getAction() == Chunk.Action.DELETED);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public boolean isContext() {
        return (getAction() == Chunk.Action.CONTEXT);
    }

    /**
     * Method description
     *
     *
     * @param action
     * @param parser
     *
     * @return
     */
    protected String toString(Action action, BasicParser parser) {
        try {
            return parser.toString(action);
        } catch (Exception ex) {
            Logger.getLogger(UnifiedChunk.class.getName()).log(Level.SEVERE, null, ex);

            return "";
        }
    }

    /**
     *
     * @return
     */
    @Override
    protected Object clone() {
        return new BasicChunk(this.getAction());
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
