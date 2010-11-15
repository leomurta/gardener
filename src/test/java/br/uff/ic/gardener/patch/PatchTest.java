
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package br.uff.ic.gardener.patch;

import br.uff.ic.gardener.patch.Patch.Format;
import br.uff.ic.gardener.patch.Patch.Match;
import br.uff.ic.gardener.patch.Patch.Type;
import br.uff.ic.gardener.util.TextHelper;
import br.uff.ic.gardener.util.UtilStream;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

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

    /** Field description */
    private InputStream patchOrigNewUnified = getResourceFile( "ori-new UnifiedFormat.txt" );

    /** Field description */
    private InputStream patchOrigNewNormal = getResourceFile( "ori-new NormalFormat.txt" );

    /** Field description */
    private InputStream patchOrigNewContext = getResourceFile( "ori-new ContextFormat.txt" );

    /** Field description */
    private InputStream patchNormal = getResourceFile( "normalFormat.txt" );

    /** Field description */
    private InputStream patchContext = getResourceFile( "contextFormat.txt" );

    /** Field description */
    private InputStream inputOriginal = getResourceFile( "original.txt" );

    /** Field description */
    private InputStream inputNew = getResourceFile( "new.txt" );

    /**
     * Test of applyPatchToFile method, of class Patch.
     *
     * @throws Exception
     */
    @Test
    public void testApplyPatchToFile_UOC() throws Exception {
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
    public void testApplyPatchToFile_UON() throws Exception {
        Format       format = Format.Unified;
        Type         type   = Type.ObjectOriented;
        Match        match  = Match.None;
        OutputStream result = Patch.applyPatchToFile( inputLao, patchUnified, format, match, type );

        assertResult( inputTzu, result );
    }

    /**
     * Test of applyPatchToFile method, of class Patch.
     *
     * @throws Exception
     */
    @Test
    public void testApplyPatchToFile_COC() throws Exception {
        Format       format = Format.Context;
        Type         type   = Type.ObjectOriented;
        Match        match  = Match.Complete;
        OutputStream result = Patch.applyPatchToFile( inputLao, patchContext, format, match, type );

        assertResult( inputTzu, result );
    }

    /**
     * Method description
     *
     *
     * @throws Exception
     */
    @Test
    public void testApplyPatchToFile_NON() throws Exception {
        Format       format = Format.Normal;
        Type         type   = Type.ObjectOriented;
        Match        match  = Match.None;
        OutputStream result = null;

        // result = Patch.applyPatchToFile( inputLao, patchNormal, format, match, type );
        // assertResult( inputTzu, result );
        result = Patch.applyPatchToFile( inputOriginal, patchOrigNewNormal, format, match, type );
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
