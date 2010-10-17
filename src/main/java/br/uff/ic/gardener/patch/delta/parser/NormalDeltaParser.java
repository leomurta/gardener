
//
//
//Generated by StarUML(tm) Java Add-In
//
//@ Project : Gardener
//@ File Name : UnifiedDeltaParser.java
//@ Date : 16/10/2010
//@ Author :
//
//
package br.uff.ic.gardener.patch.delta.parser;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.chunk.action.AddAction;
import br.uff.ic.gardener.patch.chunk.action.DelAction;
import br.uff.ic.gardener.patch.chunk.action.ModAction;
import br.uff.ic.gardener.patch.chunk.action.MovAction;
import br.uff.ic.gardener.patch.chunk.action.NoAction;
import br.uff.ic.gardener.patch.delta.Delta;
import br.uff.ic.gardener.patch.delta.parser.util.ATextDeltaParser;
import br.uff.ic.gardener.patch.delta.parser.util.Result;

//~--- JDK imports ------------------------------------------------------------

import java.io.FileInputStream;

import java.util.LinkedList;

/**
 *
 * @author Daniel
 */
public class NormalDeltaParser extends ATextDeltaParser implements DeltaParser {
    @Override
    public LinkedList<Result> parse(FileInputStream deltas) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected String headerToString(Delta delta) {

        // None
        return "";
    }

    /**
     *
     * @param action
     * @return
     */
    protected String actionToString(AddAction action) {
        return "";
    }

    /**
     *
     * @param action
     * @return
     */
    protected String actionToString(DelAction action) {
        return "";
    }

    /**
     *
     * @param action
     * @return
     */
    protected String actionToString(ModAction action) {
        return "";
    }

    /**
     *
     * @param action
     * @return
     */
    protected String actionToString(MovAction action) {
        return "";
    }

    /**
     *
     * @param action
     * @return
     */
    protected String actionToString(NoAction action) {
        return "";
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
