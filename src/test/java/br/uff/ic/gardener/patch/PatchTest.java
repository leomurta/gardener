
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package br.uff.ic.gardener.patch;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.patch.Patch.Format;
import br.uff.ic.gardener.patch.Patch.Match;
import br.uff.ic.gardener.patch.Patch.Type;
import br.uff.ic.gardener.util.TextHelper;
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

import java.io.ByteArrayOutputStream;
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

    /** Field description */
    private InputStream inputLao = getResourceFile( "lao.txt" );

    /** Field description */
    private InputStream inputTzu = getResourceFile( "tzu.txt" );

    /** Field description */
    private InputStream patchUnified = getResourceFile( "unifiedFormat.txt" );

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
    public void testApplyPatchToFile1() throws Exception {
        System.out.println( "applyPatchToFile: lao.txt->tzu.txt using unified, complete match and oo." );

        Format       format = Format.Unified;
        Type         type   = Type.ObjectOriented;
        Match        match  = Match.Complete;
        OutputStream result = Patch.applyPatchToFile( inputLao, patchUnified, format, match, type );

        assertResult( inputTzu, result );
    }

    /**
     * Test of applyPatchToFile method, of class Patch.
     *
     * @throws Exception
     */
    @Test
    public void testApplyPatchToFile2() throws Exception {
        System.out.println( "applyPatchToFile: lao.txt->tzu.txt using unified, no match and oo." );

        Format       format = Format.Unified;
        Type         type   = Type.ObjectOriented;
        Match        match  = Match.None;
        OutputStream result = Patch.applyPatchToFile( inputLao, patchUnified, format, match, type );

        assertResult( inputTzu, result );
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

    /**
     * Method description
     *
     *
     * @param input
     * @param result
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws UnsupportedEncodingException
     */
    private void assertResult( InputStream input, OutputStream result )
            throws UnsupportedEncodingException, IOException, InterruptedException {
        String sText   = UtilStream.toString( input );
        String sResult = UtilStream.toString( (ByteArrayOutputStream) result );

        // normalize breaks
        sText   = TextHelper.normalizeBreakLine( sText );
        sResult = TextHelper.normalizeBreakLine( sResult );
        assertTrue( sText.compareTo( sResult ) == 0 );
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
