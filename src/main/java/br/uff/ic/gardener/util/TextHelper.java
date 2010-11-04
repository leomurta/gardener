
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package br.uff.ic.gardener.util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
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

	public static byte[] toByteArray(char[] string) 
	{
		byte[] bytes = new byte[string.length];
		for(int i = 0; i < string.length; i++)
			bytes[i] = (byte)string[i];
		
		return bytes;
	}

	/**
	 * This method returns a relative path of a file from other. However, it does not work with dots (., ..) 
	 * Because the project do not need it.  
	 * @param pathRadix The source path (example: /public/path/a/)
	 * @param path	The absolute target (example: /public/path/b/text.txt)
	 * @return the relatived path (example: /b/text.txt)
	 */
	public static String getRelative(String strRadix, String strPath, String separatorchar) 
	{
		String[] vecRadix = strRadix.split(separatorchar);
		String[] vecPath  = strPath.split(separatorchar);
		
		int size = Math.min(vecRadix.length, vecPath.length);
		int i = 0;
		for(i = 0; i < size; i++)
		{
			if(!vecRadix[i].equals(vecPath[i]))
				break;
		}
		
		StringBuilder sb = new StringBuilder();
		for(int j = i; j < vecPath.length; j++)
		{
			sb.append(vecPath[j]);
			sb.append(separatorchar);
		}
		
		return sb.toString();
	}
}


//~ Formatted by Jindent --- http://www.jindent.com
