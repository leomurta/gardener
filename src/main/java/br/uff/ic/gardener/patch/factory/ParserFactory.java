package br.uff.ic.gardener.patch.factory;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.delta.parser.BinaryDeltaParser;
import br.uff.ic.gardener.patch.delta.parser.ContextDeltaParser;
import br.uff.ic.gardener.patch.delta.parser.DeltaParser;
import br.uff.ic.gardener.patch.delta.parser.NormalDeltaParser;
import br.uff.ic.gardener.patch.delta.parser.UnifiedDeltaParser;
import br.uff.ic.gardener.patch.format.BinaryFormat;
import br.uff.ic.gardener.patch.format.ContextFormat;
import br.uff.ic.gardener.patch.format.Format;
import br.uff.ic.gardener.patch.format.NormalFormat;
import br.uff.ic.gardener.patch.format.UnifiedFormat;

/**
 *
 * @author Daniel
 */
public class ParserFactory {
    public static DeltaParser get(Format format) throws Exception {
        if (format instanceof BinaryFormat) {
            return new BinaryDeltaParser();
        } else if (format instanceof ContextFormat) {
            return new ContextDeltaParser();
        } else if (format instanceof NormalFormat) {
            return new NormalDeltaParser();
        } else if (format instanceof UnifiedFormat) {
            return new UnifiedDeltaParser();
        } else {
            throw new Exception("Unsupported format: " + format.getClass());
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
