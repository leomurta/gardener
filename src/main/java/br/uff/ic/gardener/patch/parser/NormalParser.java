package br.uff.ic.gardener.patch.parser;

//~--- JDK imports ------------------------------------------------------------

import java.io.FileInputStream;

import java.util.LinkedList;

/**
 *
 * @author Daniel
 */
public class NormalParser extends BasicParser implements Parser {

    /**
     *
     * @param deltas
     * @return
     * @throws Exception
     */
    @Override
    public LinkedList<Result> parse(FileInputStream deltas) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
