package br.uff.ic.gardener.patch;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.delta.parser.DeltaParser;
import br.uff.ic.gardener.patch.delta.parser.util.Result;
import br.uff.ic.gardener.patch.factory.ParserFactory;
import br.uff.ic.gardener.patch.factory.PatcherFactory;
import br.uff.ic.gardener.patch.format.Format;
import br.uff.ic.gardener.patch.patcher.Patcher;

//~--- JDK imports ------------------------------------------------------------

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.LinkedList;

/**
 *
 * @author Daniel
 */
public class Patch {

    /**
     *
     * @param file
     * @param patch
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static OutputStream applyPatchToFile(InputStream file, InputStream patch, Format format)
            throws IOException, Exception {

        // Verify input streams types
        verifyInput(file, patch);

        // Create parser
        DeltaParser parser = ParserFactory.get(format);

        // Parse patch stream
        LinkedList<Result> results = parser.parse((FileInputStream) patch);

        // Create correct patcher
        Patcher patcher = PatcherFactory.get(format);

        // Apply patch to new stream
        OutputStream outStream = patcher.patch((FileInputStream) file, results);

        return outStream;
    }

    private static void verifyInput(InputStream file, InputStream patchs) throws IOException {
        if (!(file instanceof FileInputStream)) {
            throwError(file, FileInputStream.class);
        }

        if (!(patchs instanceof FileInputStream)) {
            throwError(patchs, FileInputStream.class);
        }
    }

    private static void throwError(InputStream found, java.lang.Class expected) throws IOException {
        throw new IOException("Unsuported input stream: " + "found: " + found.getClass() + "expected: "
                              + expected.toString());
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
