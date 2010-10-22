package br.uff.ic.gardener.patch.delta;

/**
 *
 * @author Daniel
 */
public class UnifiedDelta extends BasicDelta implements Delta {

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();

        text.append(infoToString("---", this.getInfo1()));
        text.append("\n");
        text.append(infoToString("+++", this.getInfo2()));
        text.append("\n");

        return text.toString();
    }

    /**
     *
     * @param ident
     * @param info
     * @return
     */
    protected String infoToString(String ident, Info info) {
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
