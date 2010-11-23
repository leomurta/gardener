package br.uff.ic.gardener.patch;

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

    /**
     *
     * @param format
     * @return
     *
     * @throws PatchException
     */
    public static Patcher get(Patch.Format format) throws PatchException {
        if (format == Patch.Format.Binary) {
            return new BinaryPatcher();
        } else if (format == Patch.Format.Context) {
            return new ContextPatcher();
        } else if (format == Patch.Format.LessContext) {
            return new ContextPatcher();
        } else if (format == Patch.Format.Normal) {
            return new NormalPatcher();
        } else if (format == Patch.Format.Unified) {
            return new UnifiedPatcher();
        } else {
            throw new PatchException(PatchException.MSG_INVALIDFORMAT);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
