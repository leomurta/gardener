package br.uff.ic.gardener.patch.patcher;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.Patch.Match;
import br.uff.ic.gardener.patch.delta.Delta;
import br.uff.ic.gardener.patch.parser.Result;

//~--- JDK imports ------------------------------------------------------------

import java.io.InputStream;
import java.io.OutputStream;

import java.util.LinkedList;

/**
 *
 * @author Daniel
 */
public class NormalPatcher extends BasicPatcher implements Patcher {

    /**
     *
     * @param results
     * @return
     */
    @Override
    public OutputStream patch(InputStream input, LinkedList<Result> results) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OutputStream patch(InputStream input, InputStream patch, Match match) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OutputStream patch(InputStream input, Delta delta, Match match) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
