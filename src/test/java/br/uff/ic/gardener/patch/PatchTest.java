
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package br.uff.ic.gardener.patch;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.Patch.Format;
import br.uff.ic.gardener.patch.Patch.Match;
import br.uff.ic.gardener.patch.Patch.Type;
import br.uff.ic.gardener.util.UtilStream;

import com.mongodb.io.StreamUtil;
import com.mongodb.util.Util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class PatchTest {

    /**
     * Constructs ...
     *
     */
    public PatchTest() {}

    /**
     * Method description
     *
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {}

    /**
     * Method description
     *
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {}

    /**
     * Method description
     *
     */
    @Before
    public void setUp() {}

    /**
     * Method description
     *
     */
    @After
    public void tearDown() {}

    /**
     * Test of applyPatchToFile method, of class Patch.
     *
     * @throws Exception
     */
    @Test
    public void testApplyPatchToFile() throws Exception {
        System.out.println( "applyPatchToFile" );

        InputStream  inputLao  = getResourceFile( "lao.txt" );
        InputStream  patch     = getResourceFile( "unifiedFormat.txt" );
        OutputStream outputTzu = getFileOutputStream( "tzu.txt" );

        /*
         * lao.txt->tzu.txt using unified, complete match and oo.
         *
         */
        {

            // Format       format = Format.Unified;
            // Type         type   = Type.ObjectOriented;
            // Match        match  = Match.Complete;
            // OutputStream result = Patch.applyPatchToFile( inputLao, patch, format, match, type );
            // assertEquals( "lao.txt->tzu.txt using unified, complete match and oo.", outputTzu, result );
        }

        /*
         * lao.txt->tzu.txt using unified, complete match and oo.
         */
        {
            Format       format = Format.Unified;
            Type         type   = Type.ObjectOriented;
            Match        match  = Match.None;
            OutputStream result = Patch.applyPatchToFile( inputLao, patch, format, match, type );

            assertEquals( "lao.txt->tzu.txt using unified, no match and oo.", outputTzu, result );
        }
    }

    /**
     * Method description
     *
     *
     * @param file
     *
     * @return
     */
    protected InputStream getResourceFile( String file ) {
        String root = "/br/uff/ic/gardener/patch/resource/";
        String path = root + file;

        return this.getClass().getResourceAsStream( path );
    }

    /**
     * Method description
     *
     *
     * @param file
     *
     * @return
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws UnsupportedEncodingException
     */
    protected OutputStream getFileOutputStream( String file )
            throws IOException, UnsupportedEncodingException, InterruptedException {
        InputStream in = getResourceFile( file );

        return UtilStream.toOutputStream( UtilStream.toString( in ) );
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
