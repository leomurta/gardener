package br.uff.ic.gardener.patch.delta;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.parser.UnifiedParser;

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

        text.append(infoToString(UnifiedParser.ORIGINAL_IDENT, this.getOriginalFileInfo()));
        text.append("\n");
        text.append(infoToString(UnifiedParser.NEW_IDENT, this.getNewFileInfo()));
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
