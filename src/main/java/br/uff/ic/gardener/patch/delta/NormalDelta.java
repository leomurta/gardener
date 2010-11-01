package br.uff.ic.gardener.patch.delta;

/**
 *
 * @author Daniel
 */
public class NormalDelta extends BasicDelta implements Delta {

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();

        text.append("@@");
        text.append(summaryToString(" -", this.getOriginalFileInfo()));
        text.append(" ");
        text.append(summaryToString(" +", this.getNewFileInfo()));
        text.append(" @@");
        text.append("\n");

        return text.toString();
    }

    /**
     *
     * @param sHeader
     * @param info
     * @return
     */
    protected String summaryToString(String sHeader, FileInfo info) {
        StringBuilder text = new StringBuilder();

        text.append(sHeader);

       
        return text.toString();
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
