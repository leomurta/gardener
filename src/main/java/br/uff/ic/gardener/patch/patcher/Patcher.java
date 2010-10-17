
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package br.uff.ic.gardener.patch.patcher;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.delta.parser.util.Result;

//~--- JDK imports ------------------------------------------------------------

import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.LinkedList;

/**
 *
 * @author Daniel
 */
public interface Patcher {
    /**
     *
     * @param file
     * @param results
     * @return
     */
    public OutputStream patch(FileInputStream file, LinkedList<Result> results);
}


//~ Formatted by Jindent --- http://www.jindent.com
