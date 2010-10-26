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
    private FileInfo      newFile;
    private FileInfo      originalFile;

    /**
     * @return the info1
     */
    public FileInfo getOriginalFileInfo() {
        return originalFile;
    }

    /**
     * @param info1 the info1 to set
     */
    public void setOriginalFileInfo(FileInfo info1) {
        this.originalFile = info1;
    }

    /**
     * @return the info2
     */
    public FileInfo getNewFileInfo() {
        return newFile;
    }

    /**
     * @param info2 the info2 to set
     */
    public void setNewFileInfo(FileInfo info2) {
        this.newFile = info2;
    }

    /**
     * @return the deltas
     */
    public LinkedList<DeltaItem> getDeltaItens() {
        return deltaItens;
    }

    /**
     * @param deltaItens
     */
    public void setDeltaItens(LinkedList<DeltaItem> deltaItens) {
        this.deltaItens = deltaItens;
    }

    public final void addDeltaItens(DeltaItem deltaItem) {
        if (getDeltaItens() == null) {
            setDeltaItens(new LinkedList<DeltaItem>());
        }

        getDeltaItens().add(deltaItem);
    }

    /**
     *
     * @return
     */
    protected String deltaItensToString() {
        return TextHelper.toString(deltaItens);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
