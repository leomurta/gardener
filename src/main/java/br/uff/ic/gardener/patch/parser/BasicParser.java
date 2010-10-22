package br.uff.ic.gardener.patch.parser;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.delta.Delta;
import br.uff.ic.gardener.util.TextHelper;

//~--- JDK imports ------------------------------------------------------------

import java.util.LinkedList;

/**
 *
 * @author Daniel
 */
public class BasicParser {

    /**
     *
     * @param deltas
     * @return String
     */
    public String toString(LinkedList<Delta> deltas) {
        return TextHelper.toString(deltas);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
