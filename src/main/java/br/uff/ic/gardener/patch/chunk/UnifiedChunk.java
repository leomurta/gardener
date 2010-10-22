package br.uff.ic.gardener.patch.chunk;

/**
 *
 * @author Daniel
 */
public class UnifiedChunk extends TextChunk implements Chunk {

    /**
     *
     * @param action
     * @param text
     */
    public UnifiedChunk(Action action, String text) {
        super(action, text);
    }

    /**
     *
     * @param action
     * @return
     * @throws Exception
     */
    @Override
    public String toString(Action action) {
        if (action == Action.Add) {
            return "+";
        } else if (action == Action.Del) {
            return "-";
        } else if (action == Action.Mod) {
            return "\\";
        } else if (action == Action.Mov) {
            return "m";
        } else if (action == Action.Non) {
            return " ";
        } else {
            return super.toString(action);
        }
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();

        text.append(toString(getAction()));
        text.append(" ");
        text.append(getText());
        text.append("\n");

        return text.toString();
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
