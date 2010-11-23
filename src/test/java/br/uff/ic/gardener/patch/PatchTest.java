
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gardener.patch;

import br.uff.ic.gardener.diff.Diff;
import br.uff.ic.gardener.diff.WriterFactory;
import br.uff.ic.gardener.patch.Patch.Format;
import br.uff.ic.gardener.patch.Patch.Match;
import br.uff.ic.gardener.patch.Patch.Type;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.junit.Before;
import br.uff.ic.gardener.util.TestHelper;

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

    /**
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        String subdir = "/patch/";
        inputLao = TestHelper.getResourceFile("lao.txt", subdir);
        inputTzu = TestHelper.getResourceFile("tzu.txt", subdir);
        patchUnified = TestHelper.getResourceFile("unifiedFormat.txt", subdir);
        patchOrigNewUnified = TestHelper.getResourceFile("ori-new UnifiedFormat.txt", subdir);
        patchOrigNewNormal = TestHelper.getResourceFile("ori-new NormalFormat.txt", subdir);
        patchOrigNewContext = TestHelper.getResourceFile("ori-new ContextFormat.txt", subdir);
        patchNormal = TestHelper.getResourceFile("normalFormat.txt", subdir);
        patchContext = TestHelper.getResourceFile("contextFormat.txt", subdir);
        inputOriginal = TestHelper.getResourceFile("original.txt", subdir);
        inputNew = TestHelper.getResourceFile("new.txt", subdir);
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
        TestHelper.assertResult(inputTzu, result);
        result = Patch.applyPatchToFile(inputOriginal, patchOrigNewUnified, format, match, type);
        TestHelper.assertResult(inputNew, result);
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
        TestHelper.assertResult(inputNew, result);
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
        TestHelper.assertResult(inputTzu, result);
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
        TestHelper.assertResult(inputNew, result);
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
        TestHelper.assertResult(inputTzu, result);
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
        TestHelper.assertResult(inputNew, result);
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
        TestHelper.assertResult(inputTzu, result);
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
        TestHelper.assertResult(inputNew, result);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testDiffPatchLaoTzuNormal() throws Exception {
        testDiffPatch("lao", "tzu", 'n', Match.None);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testDiffPatchLaoTzuContext() throws Exception {
        testDiffPatch("lao", "tzu", 'n', Match.Complete);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testDiffPatchLaoTzuUnifiedNone() throws Exception {
        testDiffPatch("lao", "tzu", 'u', Match.None);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testDiffPatchLaoTzuUnifiedComplete() throws Exception {
        testDiffPatch("lao", "tzu", 'u', Match.Complete);
    }

    /**
     *
     * @throws Exception
     */
    //@Test
    public void testDiffPatchLaoTzuLessContextComplete() throws Exception {
        testDiffPatch("lao", "tzu", 'l', Match.Complete);
    }

    /**
     *
     * @param f1
     * @param f2
     * @param format
     * @param match
     * @throws Exception
     */
    public void testDiffPatch(String f1, String f2, char format, Match match) throws Exception {
        String root = TestHelper.getCurrentPath() + "/patch/";
        File file1 = new File(root + f1 + ".txt");
        File file2 = new File(root + f2 + ".txt");

        Format fmt = null;
        if (format == 'c') {
            fmt = Format.Context;
        } else if (format == 'l') {
            fmt = Format.LessContext;
        } else if (format == 'u') {
            fmt = Format.Unified;
        } else if (format == 'n') {
            fmt = Format.Normal;
        }

        String fileOutName = root + "diff_" + format + "_" + match + ".txt";

        //delete previous test
        new File(fileOutName).delete();

        WriterFactory.setWriter(fileOutName);
        Diff diffC = new Diff(file1, file2, format);
        diffC.setOutputFormat();

        FileInputStream fi1 = new FileInputStream(file1);
        FileInputStream fi2 = new FileInputStream(file2);
        FileInputStream fiOut = new FileInputStream(fileOutName);
        OutputStream result = Patch.applyPatchToFile(fi1, fiOut, fmt, match, Type.ObjectOriented);
        TestHelper.assertResult(fi2, result);
    }
}
