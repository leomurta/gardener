package br.uff.ic.gardener.patch.delta;

/**
 * Output format based on unified.
 * @author Daniel
 */
public class BinaryDelta extends BasicDelta implements Delta {

    /**
     *
     * @return
     * @throws Exception
     */
    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();

        text.append(infoToString("---", this.getOriginalFileInfo()));
        text.append("\n");
        text.append(infoToString("+++", this.getNewFileInfo()));
        text.append("\n");

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
        text.append("\t");
        text.append(info.getPath());
        text.append("\t");
        text.append(info.getDate().toString());

        return text.toString();
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
