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
    public FileInfo getOriginalFileInfo();

    public void setOriginalFileInfo(FileInfo info);

    public FileInfo getNewFileInfo();

    public void setNewFileInfo(FileInfo info);

    public LinkedList<DeltaItem> getDeltaItens();

    public void setDeltaItens(LinkedList<DeltaItem> deltaItens);

    /**
     *
     * @return
     */
    @Override
    public String toString();
}


//~ Formatted by Jindent --- http://www.jindent.com
