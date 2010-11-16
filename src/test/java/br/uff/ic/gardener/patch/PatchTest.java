
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
import org.junit.Before;

/**
 *
 * @author Daniel
 */
public class PatchTest {

    /** Field description */
    private InputStream inputLao;
    /** Field description */
    private InputStream inputTzu;
    /** Field description */
    private InputStream patchUnified;
    /** Field description */
    private InputStream patchOrigNewUnified;
    /** Field description */
    private InputStream patchOrigNewNormal;
    /** Field description */
    private InputStream patchOrigNewContext;
    /** Field description */
    private InputStream patchNormal;
    /** Field description */
    private InputStream patchContext;
    /** Field description */
    private InputStream inputOriginal;
    /** Field description */
    private InputStream inputNew;

    @Before
    public void setUp() throws Exception {
        inputLao = getResourceFile("lao.txt");
        inputTzu = getResourceFile("tzu.txt");
        patchUnified = getResourceFile("unifiedFormat.txt");
        patchOrigNewUnified = getResourceFile("ori-new UnifiedFormat.txt");
        patchOrigNewNormal = getResourceFile("ori-new NormalFormat.txt");
        patchOrigNewContext = getResourceFile("ori-new ContextFormat.txt");
        patchNormal = getResourceFile("normalFormat.txt");
        patchContext = getResourceFile("contextFormat.txt");
        inputOriginal = getResourceFile("original.txt");
        inputNew = getResourceFile("new.txt");
    }

    /**
     * Test of applyPatchToFile method, of class Patch.
     *
     * @throws Exception
     */
    @Test
    public void testApplyPatchToFile_UOC_1() throws Exception {
        Format format = Format.Unified;
        Type type = Type.ObjectOriented;
        Match match = Match.Complete;
        OutputStream result = null;

        result = Patch.applyPatchToFile(inputLao, patchUnified, format, match, type);
        assertResult(inputTzu, result);
        result = Patch.applyPatchToFile(inputOriginal, patchOrigNewUnified, format, match, type);
        assertResult(inputNew, result);
    }

    /**
     * Method description
     *
     *
     * @throws Exception
     */
    @Test
    public void testApplyPatchToFile_UOC_2() throws Exception {
        Format format = Format.Unified;
        Type type = Type.ObjectOriented;
        Match match = Match.Complete;
        OutputStream result = null;

        result = Patch.applyPatchToFile(inputOriginal, patchOrigNewUnified, format, match, type);
        assertResult(inputNew, result);
    }

    /**
     * Test of applyPatchToFile method, of class Patch.
     *
     * @throws Exception
     */
    @Test
    public void testApplyPatchToFile_UON_1() throws Exception {
        Format format = Format.Unified;
        Type type = Type.ObjectOriented;
        Match match = Match.None;
        OutputStream result = null;

        result = Patch.applyPatchToFile(inputLao, patchUnified, format, match, type);
        assertResult(inputTzu, result);
    }

    /**
     * Method description
     *
     *
     * @throws Exception
     */
    @Test
    public void testApplyPatchToFile_UON_2() throws Exception {
        Format format = Format.Unified;
        Type type = Type.ObjectOriented;
        Match match = Match.None;
        OutputStream result = null;

        result = Patch.applyPatchToFile(inputOriginal, patchOrigNewUnified, format, match, type);
        assertResult(inputNew, result);
    }

    /**
     * Test of applyPatchToFile method, of class Patch.
     *
     * @throws Exception
     */
    @Test
    public void testApplyPatchToFile_COC_1() throws Exception {
        Format format = Format.Context;
        Type type = Type.ObjectOriented;
        Match match = Match.Complete;
        OutputStream result = null;

        result = Patch.applyPatchToFile(inputLao, patchContext, format, match, type);
        assertResult(inputTzu, result);
    }

    /**
     * Method description
     *
     *
     * @throws Exception
     */
    @Test
    public void testApplyPatchToFile_COC_2() throws Exception {
        Format format = Format.Context;
        Type type = Type.ObjectOriented;
        Match match = Match.Complete;
        OutputStream result = null;

        result = Patch.applyPatchToFile(inputOriginal, patchOrigNewContext, format, match, type);
        assertResult(inputNew, result);
    }

    /**
     * Method description
     *
     *
     * @throws Exception
     */
    @Test
    public void testApplyPatchToFile_NON_1() throws Exception {
        Format format = Format.Normal;
        Type type = Type.ObjectOriented;
        Match match = Match.None;
        OutputStream result = null;

        result = Patch.applyPatchToFile(inputLao, patchNormal, format, match, type);
        assertResult(inputTzu, result);
    }

    /**
     * Method description
     *
     *
     * @throws Exception
     */
    @Test
    public void testApplyPatchToFile_NON_2() throws Exception {
        Format format = Format.Normal;
        Type type = Type.ObjectOriented;
        Match match = Match.None;
        OutputStream result = null;

        result = Patch.applyPatchToFile(inputOriginal, patchOrigNewNormal, format, match, type);
        assertResult(inputNew, result);
    }

    /**
     * Method description
     *
     *
     * @param file
     *
     * @return
     */
    protected InputStream getResourceFile(String file) {
        String root = "/patch/";
        String path = root + file;
        InputStream in = this.getClass().getResourceAsStream(path);
        assertTrue(in != null);
        return in;
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
    protected OutputStream getFileOutputStream(String file)
            throws IOException, UnsupportedEncodingException, InterruptedException {
        InputStream in = getResourceFile(file);

        assertTrue(in != null);

        return UtilStream.toOutputStream(UtilStream.toString(in));
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
    private void assertResult(InputStream input, OutputStream result)
            throws UnsupportedEncodingException, IOException, InterruptedException {
        String sText = UtilStream.toString(input);
        String sResult = UtilStream.toString((ByteArrayOutputStream) result);

        // normalize breaks
        sText = TextHelper.normalizeBreakLine(sText);
        sResult = TextHelper.normalizeBreakLine(sResult);
        assertTrue(sText.compareTo(sResult) == 0);
    }
}
