package br.uff.ic.gardener.patch;

import br.uff.ic.gardener.patch.delta.Delta;
import br.uff.ic.gardener.patch.parser.Parser;
import br.uff.ic.gardener.patch.patcher.Patcher;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Daniel
 */
public class Patch {

    /**
     *
     */
    public enum Format {

        /**
         *
         */
        Unified,
        /**
         *
         */
        Context,
        /**
         *
         */
        LessContext,
        /**
         *
         */
        Normal,
        /**
         *
         */
        Binary
    }

    /**
     *
     */
    public enum Match {

        /**
         *
         */
        None,
        /**
         *
         */
        Complete
    }

    /**
     *
     */
    public enum Type {

        /**
         *
         */
        ObjectOriented,
        /**
         *
         */
        SAX
    }

    /**
     * Method description
     *
     *
     * @param file
     * @param patch
     * @param format
     * @param match
     * @param type
     *
     * @return
     *
     *
     * @throws PatchException
     */
    public static OutputStream applyPatchToFile(InputStream file, InputStream patch, Format format, Match match,
            Type type)
            throws PatchException {
        if (type == Type.ObjectOriented) {
            return applyPatchToFileOO(file, patch, format, match);
        } else if (type == Type.SAX) {
            return applyPatchToFileSAX(file, patch, format, match);
        } else {
            throw new PatchException(PatchException.MSG_INVALIDTYPE);
        }
    }

    /**
     *
     * @param file
     * @param patch
     * @param format
     * @param match
     * @return
     *
     * @throws PatchException
     */
    protected static OutputStream applyPatchToFileOO(InputStream file, InputStream patch, Format format, Match match)
            throws PatchException {

        // Create parser
        Parser parser = ParserFactory.get(format);

        // Create patcher
        Patcher patcher = PatcherFactory.get(format);

        // Parse patch stream
        Delta delta = parser.parseDelta(patch);

        // Apply patch to new stream
        return patcher.patch(file, delta, match);
    }

    /**
     *
     * @param file
     * @param patch
     * @param format
     * @param match
     * @return
     *
     * @throws PatchException
     */
    protected static OutputStream applyPatchToFileSAX(InputStream file, InputStream patch, Format format, Match match)
            throws PatchException {

        // Create patcher
        Patcher patcher = PatcherFactory.get(format);

        // Apply patch to new stream
        return patcher.patch(file, patch, match);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com

