
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package br.uff.ic.gardener.patch.patcher;

import java.util.LinkedList;

/**
 *
 * @author daniel
 */
public class Matcher {

    /**
     * Method description
     *
     *
     * @param startline
     * @param context
     * @param text
     *
     * @return
     */
    public static int match(int startline, LinkedList<String> context, LinkedList<String> text) {

        // No context, so no match to find
        if (context.isEmpty()) {

            // Beginning of file
            return startline;
        }

        // Matching down first
        for (int i = startline; i < text.size(); i++) {
            if (isMatchingBlock(i, context, text)) {
                return (i + context.size());
            }
        }

        // Matching up
        for (int i = 0; i < startline; i++) {
            if (isMatchingBlock(i, context, text)) {
                return (i + context.size());
            }
        }

        // No match found
        return -1;
    }

    /**
     * Method description
     *
     *
     * @param index
     * @param context
     * @param text
     *
     * @return
     */
    public static boolean isMatchingBlock(int index, LinkedList<String> context, LinkedList<String> text) {
        for (int i = 0; i < context.size(); i++) {
            String contextLine = context.get(i);
            String textLine    = text.get(index + i);

            // difference found
            if (!isMatchingLine(contextLine, textLine)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Method description
     *
     *
     * @param text1
     * @param text2
     *
     * @return
     */
    public static boolean isMatchingLine(String text1, String text2) {
        if ((text1 == null) || (text2 == null)) {
            return false;
        }

        text1 = text1.replaceAll("\r", "").replace("\n", "");
        text2 = text2.replaceAll("\r", "").replace("\n", "");

        return (text2.compareTo(text1) == 0);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
