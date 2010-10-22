package br.uff.ic.gardener.patch.chunk;

/**
 *
 * @author Daniel
 */
public class ContextChunk extends TextChunk implements Chunk {

    /**
     *
     * @param action
     * @param text
     */
    public ContextChunk(Action action, String text) {
        super(action, text);
    }

    /**
     *
     * @param action
     * @return
     * @throws Exception
     */
    public String ToString(Action action) {
        if (action == Action.Add) {
            return " ";
        } else if (action == Action.Del) {
            return " ";
        } else if (action == Action.Mod) {
            return " ";
        } else if (action == Action.Mov) {
            return " ";
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
}


//~ Formatted by Jindent --- http://www.jindent.com
