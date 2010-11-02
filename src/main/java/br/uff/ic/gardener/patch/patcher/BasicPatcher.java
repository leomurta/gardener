
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package br.uff.ic.gardener.patch.patcher;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.Patch.Match;
import br.uff.ic.gardener.patch.delta.Delta;
import br.uff.ic.gardener.util.TextHelper;
import br.uff.ic.gardener.util.UtilStream;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class BasicPatcher {

    /** Field description */
    private Delta delta;

    /** Field description */
    private InputStream input;

    /** Field description */
    private List<ApplyDeltaItemResult> lastApplyResults;

    /** Field description */
    private Match match;

    /**
     * Method description
     *
     *
     * @param input
     * @param delta
     * @param match
     */
    protected void setup( InputStream input, Delta delta, Match match ) {
        setInput( input );
        setDelta( delta );
        setMatch( match );
    }

    /**
     * @return the match
     */
    protected Match getMatch() {
        return match;
    }

    /**
     * @param match the match to set
     */
    protected void setMatch( Match match ) {
        this.match = match;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    protected boolean isCompleteMatch() {
        return getMatch().equals( Match.Complete );
    }

    /**
     * Method description
     *
     *
     * @return
     */
    protected boolean isNoMatch() {
        return getMatch().equals( Match.None );
    }

    /**
     * @return the delta
     */
    protected Delta getDelta() {
        return delta;
    }

    /**
     * @param delta the delta to set
     */
    protected void setDelta( Delta delta ) {
        this.delta = delta;
    }

    /**
     * @return the input
     */
    protected InputStream getInput() {
        return input;
    }

    /**
     * @param input the input to set
     */
    protected void setInput( InputStream input ) {
        this.input = input;
    }

    /**
     * @return the lastApplyResults
     */
    public List<ApplyDeltaItemResult> getLastApplyResults() {
        return lastApplyResults;
    }

    /**
     * @param lastApplyResults the lastApplyResults to set
     */
    public void setLastApplyResults( List<ApplyDeltaItemResult> lastApplyResults ) {
        this.lastApplyResults = lastApplyResults;
    }

    /**
     * Method description
     *
     *
     * @param text
     *
     * @return
     *
     * @throws IOException
     */
    protected OutputStream toOutpuStream( LinkedList<String> text ) throws IOException {
        return UtilStream.toOutputStream( TextHelper.toString( text ) );
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
