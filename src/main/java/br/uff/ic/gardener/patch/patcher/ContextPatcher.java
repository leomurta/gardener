package br.uff.ic.gardener.patch.patcher;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.delta.parser.util.Result;
import br.uff.ic.gardener.patch.patcher.util.APatcher;

//~--- JDK imports ------------------------------------------------------------

import java.io.FileInputStream;
import java.io.OutputStream;

import java.util.LinkedList;

/**
 *
 * @author Daniel
 */
public class ContextPatcher extends APatcher implements Patcher {

    /**
     *
     * @param file
     * @param results
     * @return
     */
    @Override
    public OutputStream patch(FileInputStream file, LinkedList<Result> results) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
