
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package br.uff.ic.gardener.patch.patcher;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.deltaitem.DeltaItem;

/**
 *
 * @author Daniel
 */
public final class ApplyDeltaItemResult {

    /** Field description */
    private DeltaItem item;

    /** Field description */
    private boolean result;

    /**
     * Constructs ...
     *
     *
     * @param item
     */
    ApplyDeltaItemResult( DeltaItem item ) {
        setItem( item );
        setResult( false );
    }

    /**
     * @return the item
     */
    public DeltaItem getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem( DeltaItem item ) {
        this.item = item;
    }

    /**
     * @return the result
     */
    public boolean isResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult( boolean result ) {
        this.result = result;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
