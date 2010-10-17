
package br.uff.ic.gardener.patch.delta.parser;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.delta.Delta;
import br.uff.ic.gardener.patch.delta.parser.util.Result;

//~--- JDK imports ------------------------------------------------------------
import java.io.FileInputStream;
import java.util.LinkedList;

/**
 *
 * @author Daniel
 */
public interface DeltaParser {

    /**
     * Parse stream containing deltas end return collection of Delta
     * @param deltas 
     * @return
     * @throws Exception
     */
    public LinkedList<Result> parse(FileInputStream deltas) throws Exception;

    /**
     *
     * @param deltas
     * @return
     * @throws Exception
     */
    public String toString(LinkedList<Delta> deltas) throws Exception;
}


//~ Formatted by Jindent --- http://www.jindent.com
