package br.uff.ic.gardener.patch.chunk;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.chunk.Chunk.Action;

/**
 *
 * @author Daniel
 */
public class TextChunk extends BasicChunk {
    private String text;

    /**
     *
     * @param action
     * @param text
     */
    protected TextChunk(Action action, String text) {
        super(action);
        setText(text);
    }

    /**
     * @return the text
     */
    public final String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public final void setText(String text) {
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
