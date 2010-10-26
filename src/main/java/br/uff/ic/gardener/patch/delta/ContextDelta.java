package br.uff.ic.gardener.patch.delta;

/**
 *
 * @author Daniel
 */
public class ContextDelta extends BasicDelta implements Delta {

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();

        text.append(infoToString("***", this.getOriginalFileInfo()));
        text.append(infoToString("---", this.getNewFileInfo()));

        return text.toString();
    }

    /**
     *
     * @param ident
     * @param info
     * @return
     */
    protected String infoToString(String ident, FileInfo info) {
        StringBuilder text = new StringBuilder();

        text.append(ident);
        text.append(" ");
        text.append(info.getPath());
        text.append("\t");
        text.append(info.getDate().toString());

        return text.toString();
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
