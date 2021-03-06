/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gardener.diff;

import java.io.File;
import org.junit.Test;
import br.uff.ic.gardener.util.TestHelper;

/**
 *
 * @author Daniel
 */
public class DiffTest {

    /**
     *
     * @throws Exception
     */
    @Test
    public void testDiffLaoTzuContext() throws Exception {
        testDiff("lao", "tzu", 'c');
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testDiffLaoTzuFullContext() throws Exception {
        testDiff("lao", "tzu", 'f');
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testDiffLeft1Right1FullContext() throws Exception {
        testDiff("left1", "right1", 'f');
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testDiffLeft2Right2FullContext() throws Exception {
        testDiff("left2", "right2", 'f');
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testDiffLeft3Right3FullContext() throws Exception {
        testDiff("left3", "right3", 'f');
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testDiffLeft4Right4FullContext() throws Exception {
        testDiff("left4", "right4", 'f');
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testDiffLeft5Right5FullContext() throws Exception {
        testDiff("left5", "right5", 'f');
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testDiffLeft6Right6FullContext() throws Exception {
        testDiff("left6", "right6", 'f');
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testDiffLaoTzuLessContext() throws Exception {
        testDiff("lao", "tzu", 'l');
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testDiffLaoTzuNormal() throws Exception {
        testDiff("lao", "tzu", 'n');
    }
    
    /**
     *
     * @throws Exception
     */
    @Test
    public void testDiffLaoTzuUnified() throws Exception {
        testDiff("lao", "tzu", 'u');
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testDiffDirectoryUnified() throws Exception {
        testDiffDirectory('u');
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testDiffDirectoryContext() throws Exception {
        testDiffDirectory('c');
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testDiffDirectoryLessContext() throws Exception {
        testDiffDirectory('l');
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void testDiffDirectoryFullContext() throws Exception {
        testDiffDirectory('f');
    }

    /**
     *
     * @param format
     * @throws Exception
     */
    public void testDiffDirectory(char format) throws Exception {
        String subDir = TestHelper.getCurrentPath() + "/diff/";
        File dir1 = new File(subDir + "/teste1");
        File dir2 = new File(subDir + "/teste2");
        String fileOutName = subDir + "diff_directory_" + format ;

        //Delete previous
        new File(fileOutName + ".txt").delete();

        WriterFactory.setWriter(fileOutName + ".txt");
        Diff diff = new Diff(dir1, dir2, format);
        diff.setOutputFormat();

        File fDiffRef = new File(fileOutName + "_ref.txt");

        TestHelper.assertResult(WriterFactory.getFile(), fDiffRef, 2);
    }

    /**
     *
     * @param orig
     * @param dest
     * @param format
     * @throws Exception
     */
    public void testDiff(String orig, String dest, char format) throws Exception {
        String subDir = TestHelper.getCurrentPath() + "/diff/";
        String ext = ".txt";
        File fOrig = new File(subDir + orig + ext);
        File fDest = new File(subDir + dest + ext);
        String fileOutName = subDir + "diff_" + format + "_" + orig + "_" + dest;

        //Delete previous
        new File(fileOutName + ext).delete();

        WriterFactory.setWriter(fileOutName + ext);
        Diff diff = new Diff(fOrig, fDest, format);
        diff.setOutputFormat();

        File fDiffRef = new File(fileOutName + "_ref" + ext);

        TestHelper.assertResult(WriterFactory.getFile(), fDiffRef, 2);
    }
}
