package br.uff.ic.gardener.patch.patcher;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.Patch;

/**
 *
 * @author Daniel
 */
public class PatcherFactory {
    /**
     *
     * @param format
     * @return
     * @throws Exception
     */
    public static Patcher get(Patch.Format format) throws Exception {
        if (format == Patch.Format.Binary) {
            return new BinaryPatcher();
        } else if (format == Patch.Format.Context) {
            return new ContextPatcher();
        } else if (format == Patch.Format.Normal) {
            return new NormalPatcher();
        } else if (format == Patch.Format.Unified) {
            return new UnifiedPatcher();
        } else {
            throw new Exception("Unsupported format: " + format.getClass());
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
