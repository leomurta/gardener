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
import java.io.OutputStream;
import br.uff.ic.gardener.util.TestHelper;

/**
 *
 * @author Daniel
 */
public class DiffPatchIntegrationTest {

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
