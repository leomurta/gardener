
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gardener.patch.patcher;

import br.uff.ic.gardener.patch.Patch.Match;
import br.uff.ic.gardener.patch.delta.Delta;
import br.uff.ic.gardener.util.TextHelper;
import br.uff.ic.gardener.util.UtilStream;

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
    protected void setup(InputStream input, Match match) throws PatcherException {
        setInput(input);
        setMatch(match);
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
    protected void setMatch(Match match) {
        this.match = match;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    protected boolean isCompleteMatch() {
        return getMatch().equals(Match.Complete);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    protected boolean isNoMatch() {
        return getMatch().equals(Match.None);
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
    protected void setInput(InputStream input) {
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
    public void setLastApplyResults(List<ApplyDeltaItemResult> lastApplyResults) {
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
     *
     * @throws PatcherException
     */
    protected OutputStream toOutpuStream(LinkedList<String> text) throws PatcherException {
        try {
            return UtilStream.toOutputStream(TextHelper.toString(text));
        } catch (IOException ex) {
            throw new PatcherException(ex);
        }
    }

    /**
     * Method description
     *
     *
     * @param input
     *
     * @return
     *
     * @throws PatcherException
     */
    protected LinkedList<String> getLines(InputStream input) throws PatcherException {
        try {
            return TextHelper.toList(UtilStream.toString(input));
        } catch (Exception ex) {
            throw new PatcherException(ex);
        }
    }

    /**
     * Method description
     *
     *
     * @param text
     * @param index
     * @param line
     *
     * @throws PatcherException
     */
    protected void verifyLineMatch(LinkedList<String> text, int index, String line) throws PatcherException {
        if(index >= text.size()){
            throw new PatcherException(PatcherException.MSG_PATCHER_DEFAULT);
        }

        if (!Matcher.isMatchingLine(text.get(index), line)) {
            throw new PatcherException(PatcherException.MSG_MATCHERROR);
        }
    }
}
