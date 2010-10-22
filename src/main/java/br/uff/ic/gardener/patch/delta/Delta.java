package br.uff.ic.gardener.patch.delta;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.deltaitem.DeltaItem;

//~--- JDK imports ------------------------------------------------------------

import java.util.LinkedList;

/**
 *
 * @author Daniel
 */
public interface Delta {

    /**
     * @return the info1
     */
    public Info getInfo1();

    /**
     * @param info1 the info1 to set
     */
    public void setInfo1(Info info1);

    /**
     * @return the info2
     */
    public Info getInfo2();

    /**
     * @param info2 the info2 to set
     */
    public void setInfo2(Info info2);

    /**
     * @return the deltas
     */
    public LinkedList<DeltaItem> getDeltaItens();

    /**
     * @param chunks
     */
    public void setDeltaItens(LinkedList<DeltaItem> deltaItens);

    /*
     *
     */
    @Override
    public String toString();
}


//~ Formatted by Jindent --- http://www.jindent.com
