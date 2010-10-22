package br.uff.ic.gardener.patch.delta;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.deltaitem.DeltaItem;
import br.uff.ic.gardener.util.TextHelper;

//~--- JDK imports ------------------------------------------------------------

import java.util.LinkedList;

/**
 *
 * @author Daniel
 */
public class BasicDelta {
    LinkedList<DeltaItem> deltaItens;
    private Info          info1;
    private Info          info2;

    /**
     * @return the info1
     */
    public Info getInfo1() {
        return info1;
    }

    /**
     * @param info1 the info1 to set
     */
    public void setInfo1(Info info1) {
        this.info1 = info1;
    }

    /**
     * @return the info2
     */
    public Info getInfo2() {
        return info2;
    }

    /**
     * @param info2 the info2 to set
     */
    public void setInfo2(Info info2) {
        this.info2 = info2;
    }

    /**
     * @return the deltas
     */
    public LinkedList<DeltaItem> getDeltaItens() {
        return deltaItens;
    }

    /**
     * @param chunks
     */
    public void setDeltaItens(LinkedList<DeltaItem> deltaItens) {
        for (DeltaItem aDeltaItem : deltaItens) {
            this.deltaItens.add((DeltaItem) aDeltaItem.clone());
        }
    }

    protected String deltaItensToString() {
        return TextHelper.toString(deltaItens);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
