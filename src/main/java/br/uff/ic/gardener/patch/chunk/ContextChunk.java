package br.uff.ic.gardener.patch.chunk;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.chunk.action.Action;
import br.uff.ic.gardener.patch.chunk.util.AChunk;

/**
 *
 * @author Daniel
 */
public class ContextChunk extends TextChunk implements Chunk {
    private String endContext;
    private String startContext;

    /**
     *
     * @param action
     * @param startContext
     * @param endContext
     * @param text
     */
    public void TextChunk(Action action, String text, String startContext, String endContext) {
        setAction(action);
        setText(text);
        setStartContext(startContext);
        setEndContext(endContext);
    }

    /**
     *
     * @return
     */
    public String getStartContext() {
        return this.startContext;
    }

    /**
     *
     * @param context
     */
    public void setStartContext(String context) {
        this.startContext = context;
    }

    /**
     *
     * @return
     */
    public String getEndContext() {
        return this.endContext;
    }

    /**
     *
     * @param context
     */
    public void setEndContext(String context) {
        this.endContext = context;
    }

    /**
     *
     * @return
     */
    @Override
    public Object clone() {
        ContextChunk clone = (ContextChunk) super.clone();

        clone.setStartContext(this.getStartContext());
        clone.setEndContext(this.getEndContext());

        return clone;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
