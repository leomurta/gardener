package br.uff.ic.gardener.patch.chunk;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.chunk.action.Action;
import br.uff.ic.gardener.patch.chunk.util.AChunk;

/**
 *
 * @author Daniel
 */
public class TextChunk extends AChunk implements Chunk {
    private String text;

    /**
     *
     * @param action
     * @param startContext
     * @param endContext
     * @param text
     */
    public void TextChunk(Action action, String text) {
        setAction(action);
        setText(text);
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @return
     */
    @Override
    public Object clone() {
        TextChunk clone = (TextChunk) super.clone();

        clone.setText(this.getText());

        return clone;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
