
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package br.uff.ic.gardener.patch.patcher;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.parser.Result;

//~--- JDK imports ------------------------------------------------------------

import java.io.FileInputStream;
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
    public OutputStream patch(InputStream input, LinkedList<Result> results) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
