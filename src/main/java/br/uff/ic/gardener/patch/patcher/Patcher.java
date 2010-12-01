
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
public interface Patcher {

    /**
     * OO version of patching
     * @param input
     * @param delta
     * @param match
     * @return
     *
     *
     * @throws PatcherException
     */
    public OutputStream patch(InputStream input, Delta delta, Match match) throws PatcherException;

    /**
     * SAX version of patching
     * @param input
     * @param patch
     * @param match
     * @return
     *
     *
     * @throws PatcherException
     */
    public OutputStream patch(InputStream input, InputStream patch, Match match) throws PatcherException;
}
