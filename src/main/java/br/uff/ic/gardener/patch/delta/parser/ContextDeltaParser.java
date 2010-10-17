package br.uff.ic.gardener.patch.delta.parser;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.chunk.Chunk;
import br.uff.ic.gardener.patch.chunk.action.AddAction;
import br.uff.ic.gardener.patch.chunk.action.DelAction;
import br.uff.ic.gardener.patch.chunk.action.ModAction;
import br.uff.ic.gardener.patch.chunk.action.MovAction;
import br.uff.ic.gardener.patch.chunk.action.NoAction;
import br.uff.ic.gardener.patch.delta.Delta;
import br.uff.ic.gardener.patch.delta.Info;
import br.uff.ic.gardener.patch.delta.parser.util.ATextDeltaParser;
import br.uff.ic.gardener.patch.delta.parser.util.Result;

//~--- JDK imports ------------------------------------------------------------

import java.io.FileInputStream;

import java.util.LinkedList;

/**
 *
 * @author Daniel
 */
public class ContextDeltaParser extends ATextDeltaParser implements DeltaParser {
    @Override
    public LinkedList<Result> parse(FileInputStream deltas) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param delta
     * @return
     */
    @Override
    protected String headerToString(Delta delta) {
        StringBuilder text = new StringBuilder();

        text.append(infoToString("***", delta.getInfo1()));
        text.append(infoToString("---", delta.getInfo2()));

        return text.toString();
    }

    /**
     *
     * @param ident
     * @param info
     * @return
     */
    protected String infoToString(String ident, Info info) {
        StringBuilder text = new StringBuilder();

        text.append(ident);
        text.append(" ");
        text.append(info.getPath());
        text.append("\t");
        text.append(info.getDate().toString());

        return text.toString();
    }

    /**
     * Default
     * @param aChunk
     * @return
     */
    @Override
    protected String chunkDataToString(Chunk aChunk) throws Exception {
        StringBuilder text = new StringBuilder();

        text.append(" ");

        return text.toString();
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
