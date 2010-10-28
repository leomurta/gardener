package br.uff.ic.gardener.patch;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.delta.Delta;
import br.uff.ic.gardener.patch.parser.Parser;
import br.uff.ic.gardener.patch.parser.ParserFactory;
import br.uff.ic.gardener.patch.parser.Result;
import br.uff.ic.gardener.patch.patcher.Patcher;
import br.uff.ic.gardener.patch.patcher.PatcherFactory;

//~--- JDK imports ------------------------------------------------------------

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
     * @throws Exception
     * @throws IOException
     */
    public static OutputStream applyPatchToFile( InputStream file, InputStream patch, Format format, Match match,
            Type type )
            throws IOException, Exception {
        if (type == Type.ObjectOriented) {
            return applyPatchToFileOO( file, patch, format, match );
        } else if (type == Type.SAX) {
            return applyPatchToFileSAX( file, patch, format, match );
        } else {
            throw new Exception( "Type not supported yet." );
        }
    }

    /**
     *
     * @param file
     * @param patch
     * @param format
     * @param match
     * @return
     * @throws IOException
     * @throws Exception
     */
    protected static OutputStream applyPatchToFileOO( InputStream file, InputStream patch, Format format, Match match )
            throws IOException, Exception {

        // Create parser
        Parser parser = ParserFactory.get( format );

        // Create patcher
        Patcher patcher = PatcherFactory.get( format );

        // Parse patch stream
        Delta delta = parser.parseDelta( patch );

        // Apply patch to new stream
        return patcher.patch( file, delta, match );
    }

    /**
     *
     * @param file
     * @param patch
     * @param format
     * @param match
     * @return
     * @throws IOException
     * @throws Exception
     */
    protected static OutputStream applyPatchToFileSAX( InputStream file, InputStream patch, Format format, Match match )
            throws IOException, Exception {

        // Create patcher
        Patcher patcher = PatcherFactory.get( format );

        // Apply patch to new stream
        OutputStream outStream = patcher.patch( file, patch, match );

        return outStream;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
