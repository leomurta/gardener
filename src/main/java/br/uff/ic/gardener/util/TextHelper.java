
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package br.uff.ic.gardener.util;

//~--- JDK imports ------------------------------------------------------------

import java.util.Arrays;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 *
 * @author Daniel
 */
public class TextHelper {

    /**
     * Method description
     *
     *
     * @param list
     *
     * @return
     */
    public static String toString( LinkedList list ) {
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
     * @param list
     * @param linebreak
     *
     * @return
     */
    public static String toString( LinkedList list, boolean linebreak ) {
        StringBuilder text = new StringBuilder();

        if (linebreak) {
            String breakLine = "\n";

            for (Object aItem : list) {
                text.append( aItem.toString() );
                text.append( breakLine );
            }
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
}


//~ Formatted by Jindent --- http://www.jindent.com
