
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gardener.patch;

import br.uff.ic.gardener.patch.Patch.Format;
import br.uff.ic.gardener.patch.Patch.Match;
import br.uff.ic.gardener.patch.Patch.Type;
import org.junit.Test;
import java.io.InputStream;
import java.io.OutputStream;
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

    /**
     *
     * @throws Exception
     */
    @Test
    public void testApplyPatchToFile_UC_2() throws Exception {
        testApplyPatchToFile(Format.Unified, Match.Complete, "ori", "new");
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testApplyPatchToFile_UN_1() throws Exception {
        testApplyPatchToFile(Format.Unified, Match.None, "lao", "tzu");
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testApplyPatchToFile_UN_2() throws Exception {
        testApplyPatchToFile(Format.Unified, Match.None, "ori", "new");
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testApplyPatchToFile_CC_1() throws Exception {
        testApplyPatchToFile(Format.Context, Match.Complete, "lao", "tzu");
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testApplyPatchToFile_LC_1() throws Exception {
        testApplyPatchToFile(Format.LessContext, Match.Complete, "lao", "tzu");
    }

    /**
     *
     * @throws Exception
     */
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

    /**
     *
     * @param format
     * @param match
     * @param input
     * @param output
     * @throws Exception
     */
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

    /**
     *
     * @param format
     * @param type
     * @param match
     * @param input
     * @param output
     * @param patch
     * @throws Exception
     */
    protected void testApplyPatchToFile(Format format,Type type,Match match,String input,String output,String patch) throws Exception {
        String subdir = "/patch/";
        String ext = ".txt";
        InputStream inputFile = TestHelper.getResourceFile(input + ext, subdir);
        InputStream outputFile = TestHelper.getResourceFile(output + ext, subdir);
        InputStream patchFile = TestHelper.getResourceFile(patch + ext, subdir);

        OutputStream result = Patch.applyPatchToFile(inputFile, patchFile, format, match, type);
        TestHelper.assertResult(outputFile, result);
    }
    
}
