/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gardener.util;

import br.uff.ic.gardener.util.TextHelper;
import br.uff.ic.gardener.util.UtilStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import static org.junit.Assert.*;

/**
 *
 * @author Daniel
 */
public class TestHelper {

    /**
     *
     * @return
     */
    public static String getCurrentPath() {
        String currentDir = System.getProperty("user.dir");
        return currentDir + "/target/test-classes/";
    }

    /**
     * Method description
     *
     *
     * @param file
     *
     * @param suddirectory
     * @return
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws UnsupportedEncodingException
     */
    public static OutputStream getFileOutputStream(String file, String suddirectory)
            throws IOException, UnsupportedEncodingException, InterruptedException {
        InputStream in = getResourceFile(file, suddirectory);

        assertTrue(in != null);

        return UtilStream.toOutputStream(UtilStream.toString(in));
    }

    /**
     * Method description
     *
     *
     * @param file
     *
     * @param suddirectory
     * @return
     */
    public static InputStream getResourceFile(String file, String suddirectory) {
        String path = suddirectory + file;
        InputStream in = path.getClass().getResourceAsStream(path);
        assertTrue(in != null);
        return in;
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
    public static void assertResult(InputStream input, OutputStream result)
            throws UnsupportedEncodingException, IOException, InterruptedException {
        String sText = UtilStream.toString(input);
        String sResult = UtilStream.toString((ByteArrayOutputStream) result);

        assertResult(sText, sResult, 0);
    }

    /**
     *
     * @param file1
     * @param file2
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @throws InterruptedException
     */
    public static void assertResult(File file1, File file2, int firstIgnoredLines)
            throws FileNotFoundException, UnsupportedEncodingException, IOException, InterruptedException {
        FileInputStream fi1 = new FileInputStream(file1);
        FileInputStream fi2 = new FileInputStream(file2);
        String sFi1 = UtilStream.toString(fi1);
        String sFi2 = UtilStream.toString(fi2);

        assertResult(sFi1, sFi2, firstIgnoredLines);
    }

    private static void assertResult(String sText, String sResult, int firstIgnoredLines) {
        // normalize breaks
        sText = TextHelper.normalizeBreakLine(sText);
        sResult = TextHelper.normalizeBreakLine(sResult);

        //Remove ignored lines
        String breakLine = "\r\n";
        for (int i = 0; i < firstIgnoredLines; i++) {
            sText = sText.substring(sText.indexOf(breakLine) + breakLine.length());
            sResult = sResult.substring(sResult.indexOf(breakLine) + breakLine.length());
        }

        assertTrue(sText.compareTo(sResult) == 0);
    }
}
