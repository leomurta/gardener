package br.uff.ic.gardener.patch;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.Patch;
import br.uff.ic.gardener.patch.parser.BinaryParser;
import br.uff.ic.gardener.patch.parser.ContextParser;
import br.uff.ic.gardener.patch.parser.Parser;
import br.uff.ic.gardener.patch.parser.NormalParser;
import br.uff.ic.gardener.patch.parser.UnifiedParser;

/**
 *
 * @author Daniel
 */
public class ParserFactory {
    public static Parser get(Patch.Format format) throws Exception {
        if (format == Patch.Format.Binary) {
            return new BinaryParser();
        } else if (format == Patch.Format.Context) {
            return new ContextParser();
        } else if (format == Patch.Format.Normal) {
            return new NormalParser();
        } else if (format == Patch.Format.Unified) {
            return new UnifiedParser();
        } else {
            throw new Exception("Unsupported format: " + format.getClass());
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
