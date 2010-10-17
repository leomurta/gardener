package br.uff.ic.gardener.patch.factory;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.format.BinaryFormat;
import br.uff.ic.gardener.patch.format.ContextFormat;
import br.uff.ic.gardener.patch.format.Format;
import br.uff.ic.gardener.patch.format.NormalFormat;
import br.uff.ic.gardener.patch.format.UnifiedFormat;
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
    public static Patcher get(Format format) throws Exception {
        if (format instanceof BinaryFormat) {
            return new BinaryPatcher();
        } else if (format instanceof ContextFormat) {
            return new ContextPatcher();
        } else if (format instanceof NormalFormat) {
            return new NormalPatcher();
        } else if (format instanceof UnifiedFormat) {
            return new UnifiedPatcher();
        } else {
            throw new Exception("Unsupported format: " + format.getClass());
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
