
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

    /**
     * Test of applyPatchToFile method, of class Patch.
     *
     * @throws Exception
     */
    @Test
    public void testApplyPatchToFile_UC_1() throws Exception {
        testApplyPatchToFile(Format.Unified, Match.Complete, "lao", "tzu");
    }

    @Test
    public void testApplyPatchToFile_UC_2() throws Exception {
        testApplyPatchToFile(Format.Unified, Match.Complete, "ori", "new");
    }

    @Test
    public void testApplyPatchToFile_UN_1() throws Exception {
        testApplyPatchToFile(Format.Unified, Match.None, "lao", "tzu");
    }

    @Test
    public void testApplyPatchToFile_UN_2() throws Exception {
        testApplyPatchToFile(Format.Unified, Match.None, "ori", "new");
    }

    @Test
    public void testApplyPatchToFile_CC_1() throws Exception {
        testApplyPatchToFile(Format.Context, Match.Complete, "lao", "tzu");
    }

    @Test
    public void testApplyPatchToFile_LC_1() throws Exception {
        testApplyPatchToFile(Format.LessContext, Match.Complete, "lao", "tzu");
    }

    @Test
    public void testApplyPatchToFile_CC_2() throws Exception {
        testApplyPatchToFile(Format.Context, Match.Complete, "ori", "new");
    }

    /**
     * Method description
     *
     *
     * @throws Exception
     */
    @Test
    public void testApplyPatchToFile_NN_1() throws Exception {
        testApplyPatchToFile(Format.Normal, Match.None, "lao", "tzu");
    }

    /**
     * Method description
     *
     *
     * @throws Exception
     */
    @Test
    public void testApplyPatchToFile_NN_2() throws Exception {
        testApplyPatchToFile(Format.Normal, Match.None, "ori", "new");
    }

    protected void testApplyPatchToFile(Format format,Match match, String input,String output) throws Exception {
        Type type = Type.ObjectOriented;

        String fmt = "";
        if (format == Format.Context) {
            fmt = "c";
        } else if (format == Format.LessContext) {
            fmt = "l";
        } else if (format == Format.Unified) {
            fmt = "u";
        } else if (format == Format.Normal) {
            fmt = "n";
        }

        String patchFile = "patch_"+input+"_"+output+"_"+fmt;

        testApplyPatchToFile(format, type, match, input, output, patchFile);
    }

    protected void testApplyPatchToFile(Format format,Type type,Match match,String input,String output,String patch) throws Exception {
        String subdir = "/patch/";
        String ext = ".txt";
        InputStream inputFile = TestHelper.getResourceFile(input + ext, subdir);
        InputStream outputFile = TestHelper.getResourceFile(output + ext, subdir);
        InputStream patchFile = TestHelper.getResourceFile(patch + ext, subdir);

        OutputStream result = Patch.applyPatchToFile(inputFile, patchFile, format, match, type);
        TestHelper.assertResult(outputFile, result);
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
    @Test
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
