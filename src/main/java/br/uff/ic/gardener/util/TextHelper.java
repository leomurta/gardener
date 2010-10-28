
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package br.uff.ic.gardener.util;

//~--- JDK imports ------------------------------------------------------------

import java.util.Arrays;
import java.util.LinkedList;

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
    public static String toString(LinkedList list) {
        return toString(list, true);
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
     * @param separator
     *
     * @return
     */
    public static String[] toArray( String text, String separator ) {
        String[] array = text.split( separator );

        // Remove separator from end of line, if exists
        for (int i = 0; i < array.length; i++) {
            if (array[i].endsWith( separator )) {
                array[i] = array[i].substring( 0, array[i].length() - separator.length() );
            }
        }

        return array;
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
    public static String toString(LinkedList list, boolean linebreak) {
        StringBuilder text = new StringBuilder();

        if (linebreak) {
            String breakLine = "\n";

            for (Object aItem : list) {
                text.append(aItem.toString());
                text.append( breakLine );
            }
        } else {
            for (Object aItem : list) {
                text.append(aItem.toString());
            }
        }

        return text.toString();
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
