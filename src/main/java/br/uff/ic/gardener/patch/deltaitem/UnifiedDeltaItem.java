package br.uff.ic.gardener.patch.deltaitem;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.chunk.Chunk;

//~--- JDK imports ------------------------------------------------------------

import java.util.LinkedList;

/**
 *
 * @author Daniel
 */
public class UnifiedDeltaItem extends BasicDeltaItem implements DeltaItem {
    public UnifiedDeltaItem(Info info1, Info info2, LinkedList<Chunk> chunks) {
        super(info1, info2, chunks);
    }

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

        if (info.getLenght() == 0) {
            text.append(info.getStart());
            text.append(",0");
        } else if (info.getLenght() == 1) {
            text.append((info.getStart() + 1));
        } else {
            text.append((info.getStart() + 1));
            text.append(",");
            text.append(info.getLenght());
        }

        return text.toString();
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
