package br.uff.ic.gardener.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.StringTokenizer;

/**
 *
 * @author Daniel
 */
public class TextHelper {

    /**
     * Method description
     * @param <T>
     *
     *
     * @param list
     *
     * @return
     */
    public static <T> String toString( Iterable<T> list ) {
        return toString( list, true );
    }

    /**
     * Method description
     *
     *
     * @param lines
     *
     * @return
     */
    public static String toString( String[] lines ) {
        LinkedList<String> list = new LinkedList<String>();

        list.addAll( Arrays.asList( lines ) );

        return toString( list, true );
    }

    /**
     * Method description
     *
     *
     * @param sText
     *
     * @return
     */
    public static String[] toArray( String sText ) {
        return toArray( sText, "\n" );
    }

    /**
     * Method description
     *
     *
     * @param text
     * @param delim
     *
     * @return
     */
    public static String[] toArray( String text, String delim ) {
        StringTokenizer tknizer = new StringTokenizer( text, delim, false );
        String[]        tokens  = new String[tknizer.countTokens()];
        int             i       = 0;

        while (tknizer.hasMoreTokens()) {

            // remove line breaks
            tokens[i++] = tknizer.nextToken().replace( "\n", "" ).replace( "\r", "" );
        }

        return tokens;
    }

    /**
     * Method description
     *
     *
     * @param text
     *
     * @return
     */
    public static LinkedList<String> toList( String text ) {
        LinkedList<String> list = new LinkedList<String>();

        list.addAll( Arrays.asList( toArray( text ) ) );

        return list;
    }

    /**
     * Method description
     * @param <T>
     *
     *
     * @param list
     * @param linebreak
     *
     * @return
     */
    public static <T> String toString( Iterable<T> list, boolean linebreak ) {
        StringBuilder text = new StringBuilder();

        if (linebreak) {
            String breakLine = "\n";

            for (Object aItem : list) {
                text.append( aItem.toString() );
                text.append( breakLine );
            }

            // remove last break
            text = text.delete( text.length() - breakLine.length(), text.length() );
        } else {
            for (Object aItem : list) {
                text.append( aItem.toString() );
            }
        }

        return text.toString();
    }

    /**
     * Method description
     *
     *
     * @param text
     *
     * @return
     */
    public static String normalizeBreakLine( String text ) {
        return text.replace( "\r", "" ).replace( "\n", "\r\n" );
    }

    /**
     * Method description
     *
     *
     * @param string
     *
     * @return
     */
    public static byte[] toByteArray( char[] string ) {
        byte[] bytes = new byte[string.length];

        for (int i = 0; i < string.length; i++) {
            bytes[i] = (byte) string[i];
        }

        return bytes;
    }

    /**
     * This method returns a relative path of a file from other. However, it does not work with dots (., ..)
     * Because the project do not need it.
     *
     * @param strRadix
     * @param strPath
     * @param separatorchar
     * @return the relatived path (example: /b/text.txt)
     */
    public static String getRelative( String strRadix, String strPath, String separatorchar ) {
        String[] vecRadix = strRadix.split( separatorchar );
        String[] vecPath  = strPath.split( separatorchar );
        int      size     = Math.min( vecRadix.length, vecPath.length );
        int      i        = 0;

        for (i = 0; i < size; i++) {
            if (!vecRadix[i].equals( vecPath[i] )) {
                break;
            }
        }

        StringBuilder sb = new StringBuilder();

        for (int j = i; j < vecPath.length; j++) {
            sb.append( vecPath[j] );
            sb.append( separatorchar );
        }

        return sb.toString();
    }

    /**
     * Method description
     *
     *
     * @param size
     *
     * @return
     */
    public static String randomString( int size ) {
        char[] c = new char[size];
        Random r = new Random();

        for (int i = 0; i < size; i++) {
            c[i] = (char) ('a' + r.nextInt( 'z' - 'a' ));
        }

        return new String( c );
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
