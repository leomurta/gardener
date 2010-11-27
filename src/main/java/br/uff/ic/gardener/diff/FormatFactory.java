package br.uff.ic.gardener.diff;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Daniel
 */
public class FormatFactory {

    /**
     *
     */
    public final String END_CONTEXT = "***************";
    private static String mainHeader = null;

    /**
     *
     * @param formatName
     * @return
     */
    public static IFormat getFormatter(char formatName) {
        switch (formatName) {
            case 'c': //CONTEXT-FORMAT
                return getContextFormatter();
            case 'l': // LESS-CONTEXT-FORMAT
                return getLessContextFormatter();
            case 'f': //FULL-CONTEXT-FORMAT
                return getFullContextFormatter();
            case 'n':  //NORMAL-FORMAT
                return getNormalFormatter();
            case 'u':  //UNIFIED-FORMAT
                return getUnifiedFormatter();
        }
        return getUnifiedFormatter();
    }

    /**
     *
     * @return
     */
    public static IFormat getNormalFormatter() {
        return new NormalFormat();
    }

    /**
     *
     * @return
     */
    public static IFormat getContextFormatter() {
        return new ContextFormat();
    }

    /**
     *
     * @return
     */
    public static IFormat getFullContextFormatter() {
        return new FullContextFormat();
    }

    /**
     *
     * @return
     */
    public static IFormat getLessContextFormatter() {
        return new LessContextFormat();
    }

    /**
     *
     * @return
     */
    public static IFormat getUnifiedFormatter() {
        return new UnifiedFormat();
    }

    /**
     *
     * @param date
     * @return
     */
    public static String getFormatDateHour(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.mms").format(date);
    }

    /**
     *
     * @param date
     * @return
     */
    public static String getDateHour(Date date) {
        return new SimpleDateFormat("yyMMddhhmm").format(date);
    }

    /**
     *
     * @param file1
     * @param file2
     */
    public static void setMainHeader(File file1, File file2) {
        //*** from-file from-file-modification-time
        //--- to-file to-file-modification time

        String headerF1 = "*** " + file1.getName().split("\\.")[0] + " " + getFormatDateHour(new Date(file1.lastModified())) + " -0800";
        String headerF2 = "--- " + file2.getName().split("\\.")[0] + " " + getFormatDateHour(new Date(file2.lastModified())) + " -0800";
        mainHeader = headerF1 + "!!" + headerF2;
    }

    /**
     *
     * @return
     */
    public static String getMainHeader() {
        return mainHeader;
    }
}
