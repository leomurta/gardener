package br.uff.ic.gardener.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import java.util.Random;

/**
 * Classe para operações rotineiras de manipulação de Streams
 */
public class UtilStream {

    /** Field description */
    public static final int DEFAULT_BUFFER_SIZE = 2156;
    /** Field description */
    private static byte[] BUFFER = new byte[DEFAULT_BUFFER_SIZE];
    /** Field description */
    private static String ENCONDING = "UTF-8";
    /** Field description */
    private static char[] CHARBUFFER = new char[DEFAULT_BUFFER_SIZE];

    /**
     * Method description
     *
     *
     * @param os
     * @param strVec
     *
     * @throws IOException
     */
    public static void fillStream(OutputStream os, String... strVec) throws IOException {

        // PrintWriter pw = new PrintWriter(os, false);
        PrintStream ps = new PrintStream(os);

        for (String str : strVec) {
            ps.format("%s%s", str, getLineSeperator());
        }

        ps.flush();
    }

    /**
     * Method description
     *
     *
     * @param ps
     * @param strVec
     *
     * @throws IOException
     */
    public static void fillPrintStream(PrintStream ps, String... strVec) throws IOException {
        for (String str : strVec) {
            ps.format("%s%s", str, getLineSeperator());
        }
    }

    /**
     * Preenche um arquivo com várias linhas de String
     * @param file o arquivo a ser preenchido
     * @param strVec as linhas que o preencherão
     *
     * @throws IOException
     */
    public static void fillFile(File file, String... strVec) throws IOException {
        FileWriter fw;

        fw = new FileWriter(file);

        PrintWriter pw = new PrintWriter(fw);

        for (String str : strVec) {
            pw.append(str + getLineSeperator());
        }

        pw.close();
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public static String getLineSeperator() {
        return System.getProperty("line.separator");
    }

    /**
     *
     * @param input
     * @param output
     * @throws IOException
     */
    public static void copy(InputStream input, OutputStream output) throws IOException {
        copy(input, output, BUFFER);
    }

    /**
     *
     * @param input
     * @param output
     * @param sizeBuffer
     * @throws IOException
     */
    public static void copy(InputStream input, OutputStream output, int sizeBuffer) throws IOException {
        byte[] buffer = null;

        if (sizeBuffer != DEFAULT_BUFFER_SIZE) {
            buffer = new byte[sizeBuffer];
        } else {
            buffer = BUFFER;
        }

        copy(input, output, buffer);
    }

    /**
     *
     * @param input
     * @param output
     * @param buffer
     * @throws IOException
     */
    public static void copy(InputStream input, OutputStream output, byte[] buffer) throws IOException {
        int byteRead = 0;

        while ((byteRead = input.read(buffer)) > 0) {
            output.write(buffer, 0, byteRead);
        }
    }
    
    public static void copy(InputStream input, OutputStream... vec) throws IOException {
    	copy(input, BUFFER, vec);
    }
    
    /**
    * Copy to varius outputstreams
    * @param input
    * @param output
    * @param buffer
    * @throws IOException
    */
   public static void copy(InputStream input, byte[] buffer, OutputStream... vec) throws IOException {
       int byteRead = 0;

       while ((byteRead = input.read(buffer)) > 0) {
           for(OutputStream o: vec)
        	   o.write(buffer, 0, byteRead);
       }
   }

    /**
     * Convert InputStream to String (only support UTF-8).
     * @param input
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @throws InterruptedException
     */
    public static String toString(InputStream input)
            throws UnsupportedEncodingException, IOException, InterruptedException {
        StringBuilder text = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(input, ENCONDING);
        int read = 0;

        read = reader.read(CHARBUFFER);

        while (read > -1) {
            if (read > 0) {
                text.append(CHARBUFFER, 0, read);
            } else {
                Thread.sleep(50);
            }

            read = reader.read(CHARBUFFER);
        }

        reader.close();

        return text.toString();
    }

    /**
     * Method description
     *
     *
     * @param output
     *
     * @return
     *
     * @throws UnsupportedEncodingException
     */
    public static String toString(ByteArrayOutputStream output) throws UnsupportedEncodingException {
        return output.toString(ENCONDING);
    }

    /**
     * Method description
     *
     *
     * @param text
     *
     * @return
     *
     * @throws IOException
     */
    public static OutputStream toOutputStream(String text) throws IOException {
        byte[] stringByteArray = text.getBytes();
        ByteArrayOutputStream out = new ByteArrayOutputStream(stringByteArray.length);

        out.write(stringByteArray);
        out.close();

        return out;
    }

    /**
     * Create random data to a OutputStream
     *
     * @param out
     * @param size
     * @throws IOException
     */
    public static void fillRandomData(OutputStream out, int size) throws IOException {
        Random r = new Random();

        while (size > 0) {
            out.write(r.nextInt());
            size--;
        }
    }

    /**
     * Create content witch each line is it line number.
     *
     * @param out
     * @param lineCount
     */
    public static void fillLineNumber(OutputStream out, int lineCount) {
        PrintStream p = new PrintStream(out);

        for (int i = 0; i < lineCount; i++) {
            p.printf("%d%s", i, UtilStream.getLineSeperator());
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com

