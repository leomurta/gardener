package br.uff.ic.gardener.patch.chunk;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.parser.NormalParser;

/**
 *
 * @author Daniel
 */
public class NormalChunk extends TextChunk implements Chunk {

    /**
     *
     * @param action
     * @param text
     */
    public NormalChunk(Action action, String text) {
        super(action, text);
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();

        text.append(toString(getAction(),new NormalParser()));
        text.append(" ");
        text.append(getText());
        text.append("\n");

        return text.toString();
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
