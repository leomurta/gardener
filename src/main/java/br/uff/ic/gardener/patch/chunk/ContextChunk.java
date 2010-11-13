package br.uff.ic.gardener.patch.chunk;

import br.uff.ic.gardener.patch.parser.ContextParser;

/**
 *
 * @author Daniel
 */
public class ContextChunk extends TextChunk implements Chunk {

    /** Indicates that is from original file */
    private boolean original;

    /**
     *
     * @param action
     * @param text
     * @param original
     */
    public ContextChunk(Action action, String text, boolean original) {
        super(action, text);
        setOriginal(original);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();

        text.append(toString(getAction(), new ContextParser()));
        text.append(" ");
        text.append(getText());
        text.append("\n");

        return text.toString();
    }

    /**
     * @return the original
     */
    public boolean isOriginal() {
        return original;
    }

    /**
     * @param original the original to set
     */
    public void setOriginal(boolean original) {
        this.original = original;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
