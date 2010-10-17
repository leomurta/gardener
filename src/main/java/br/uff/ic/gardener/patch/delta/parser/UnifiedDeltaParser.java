package br.uff.ic.gardener.patch.delta.parser;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.chunk.Chunk;
import br.uff.ic.gardener.patch.chunk.TextChunk;
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
public class UnifiedDeltaParser extends ATextDeltaParser implements DeltaParser {
    @Override
    public LinkedList<Result> parse(FileInputStream deltas) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected String headerToString(Delta delta) {
        StringBuilder text = new StringBuilder();

        Info info1 = delta.getInfo1();
        Info info2 = delta.getInfo2();

        text.append(infoToString("---", info1));
        text.append("\n");
        text.append(infoToString("+++", info2));
        text.append("\n");

        return text.toString();
    }

    @Override
    protected String deltaToString(Delta delta) {
        StringBuilder text = new StringBuilder();

        Info info1 = delta.getInfo1();
        Info info2 = delta.getInfo2();
        
        text.append("@@");
        text.append(summaryToString(" -",info1));
        text.append(" ");
        text.append(summaryToString(" +",info2));
        text.append(" @@");
        text.append("\n");

        return text.toString();
    }

    /**
     *
     * @param sHeader
     * @param info
     * @return
     */
    protected String summaryToString(String sHeader, Info info) {
        StringBuilder text = new StringBuilder();

        text.append(sHeader);

        if (info.getLenght() == 0) {
        text.append( info.getStart() + ",0");
        } else if (info.getLenght() == 1) {
        text.append( (info.getStart() + 1) );
        } else {
        text.append( (info.getStart() + 1) + "," + info.getLenght() );
        }
        
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
        text.append("\t");
        text.append(info.getPath());
        text.append("\t");
        text.append(info.getDate().toString());

        return text.toString();
    }

    /**
     * Default
     * @param aChunk
     * @return
     * @throws Exception
     */
    @Override
    protected String chunkToString(Chunk aChunk) throws Exception {
        StringBuilder text = new StringBuilder();

        // Action
        text.append(actionToString(aChunk.getAction()));

        // Chunk data
        text.append(chunkDataToString(aChunk));

        return text.toString();
    }

    /**
     *
     * @param action
     * @return
     */
    protected String actionToString(AddAction action) {
        return "+";
    }

    /**
     *
     * @param action
     * @return
     */
    protected String actionToString(DelAction action) {
        return "-";
    }

    /**
     *
     * @param action
     * @return
     */
    protected String actionToString(ModAction action) {
        return "\\";
    }

    /**
     *
     * @param action
     * @return
     */
    protected String actionToString(MovAction action) {
        return "|";
    }

    /**
     *
     * @param action
     * @return
     */
    protected String actionToString(NoAction action) {
        return " ";
    }

    /**
     *
     * @param chunk
     * @return
     * @throws Exception
     */
    @Override
    protected String chunkDataToString(Chunk chunk) throws Exception {

        if(!(chunk instanceof TextChunk)){
            throw new Exception("Unsupported chunck instance: " + chunk.getClass());
        }

        TextChunk tChunk = (TextChunk) chunk;

        StringBuilder text = new StringBuilder();

        // Action
        text.append(tChunk.getText());

        return text.toString();
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
