
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package br.uff.ic.gardener.patch.patcher;



import br.uff.ic.gardener.patch.Patch.Match;
import br.uff.ic.gardener.patch.delta.Delta;
import br.uff.ic.gardener.patch.parser.Result;




import java.io.InputStream;
import java.io.OutputStream;

import java.util.LinkedList;

/**
 *
 * @author Daniel
 */
public class BinaryPatcher extends BasicPatcher implements Patcher {

    /**
     *
     * @param input
     * @param results
     * @return
     */
    @Override
    public OutputStream patch(InputStream input, LinkedList<Result> results) throws PatcherException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OutputStream patch(InputStream input, InputStream patch, Match match) throws PatcherException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OutputStream patch(InputStream input, Delta delta, Match match) throws PatcherException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
