package br.uff.ic.gardener.patch.delta;

/**
 *
 * @author Daniel
 */
public class NormalDelta extends BasicDelta implements Delta {

    /**
     *
     * @return
     * @throws Exception
     */
    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();

        text.append("@@");
        text.append(summaryToString(" -", this.getInfo1()));
        text.append(" ");
        text.append(summaryToString(" +", this.getInfo2()));
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
    protected String summaryToString(String sHeader, Info info) {
        StringBuilder text = new StringBuilder();

        text.append(sHeader);

       
        return text.toString();
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
