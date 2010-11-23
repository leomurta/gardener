package br.uff.ic.gardener.patch;


import br.uff.ic.gardener.patch.parser.BinaryParser;
import br.uff.ic.gardener.patch.parser.ContextParser;
import br.uff.ic.gardener.patch.parser.NormalParser;
import br.uff.ic.gardener.patch.parser.Parser;
import br.uff.ic.gardener.patch.parser.UnifiedParser;

/**
 *
 * @author Daniel
 */
public class ParserFactory {

    /**
     *
     * @param format
     * @return
     *
     * @throws PatchException
     */
    public static Parser get(Patch.Format format) throws PatchException {
        if (format == Patch.Format.Binary) {
            return new BinaryParser();
        } else if (format == Patch.Format.Context) {
            return new ContextParser();
        } else if (format == Patch.Format.LessContext) {
            return new ContextParser();
        } else if (format == Patch.Format.Normal) {
            return new NormalParser();
        } else if (format == Patch.Format.Unified) {
            return new UnifiedParser();
        } else {
            throw new PatchException(PatchException.MSG_INVALIDFORMAT);
        }
    }
}
