package br.uff.ic.gardener.patch.chunk;

import br.uff.ic.gardener.patch.parser.ContextParser;

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

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();

        text.append(toString(getAction(),new ContextParser()));
        text.append(" ");
        text.append(getText());
        text.append("\n");

        return text.toString();
    }

}


//~ Formatted by Jindent --- http://www.jindent.com
