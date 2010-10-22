package br.uff.ic.gardener.patch;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.Patch;
import br.uff.ic.gardener.patch.patcher.BinaryPatcher;
import br.uff.ic.gardener.patch.patcher.ContextPatcher;
import br.uff.ic.gardener.patch.patcher.NormalPatcher;
import br.uff.ic.gardener.patch.patcher.Patcher;
import br.uff.ic.gardener.patch.patcher.UnifiedPatcher;

/**
 *
 * @author Daniel
 */
public class PatcherFactory {
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
