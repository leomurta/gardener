
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package br.uff.ic.gardener.util;

//~--- JDK imports ------------------------------------------------------------

import java.util.LinkedList;

/**
 *
 * @author Daniel
 */
public class TextHelper {
    public static String toString(LinkedList list) {
        return toString(list, true);
    }

    public static String toString(LinkedList list, boolean linebreak) {
        StringBuilder text = new StringBuilder();

        if (linebreak) {
            for (Object aItem : list) {
                text.append(aItem.toString());
                text.append("\n");
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
