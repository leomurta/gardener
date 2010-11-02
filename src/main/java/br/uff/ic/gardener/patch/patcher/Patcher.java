
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package br.uff.ic.gardener.patch.patcher;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.Patch.Match;
import br.uff.ic.gardener.patch.delta.Delta;
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
public interface Patcher {

    /**
     * OO version
     * @param input
     * @param results
     * @return
     */
    public OutputStream patch(InputStream input, LinkedList<Result> results) throws Exception;

    /**
     *
     * @param input
     * @param delta
     * @return
     */
    public OutputStream patch(InputStream input, Delta delta, Match match) throws Exception;

    /**
     * SAX version
     * @param input
     * @param results
     * @return
     */
    public OutputStream patch(InputStream input, InputStream patch, Match match) throws Exception;
}


//~ Formatted by Jindent --- http://www.jindent.com
