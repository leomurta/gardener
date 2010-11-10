package br.uff.ic.gardener.diff;
/* -------------- DETAILS DESCRIPTION OF LESS CONTEXT FORMAT --------------
 *  ***************
 *  *** from-file-line-numbers ****
 * from-file-line
 * from-file-line...
 * --- to-file-line-numbers ----
 * to-file-line
 * to-file-line...
 *
 * ‘!’ A line that is part of a group of one or more lines that changed between the two files. There is a corresponding group of lines marked with ‘!’ in the part of this hunk for the other file.
 * ‘+’ An "inserted" line in the second file that corresponds to nothing in the first file.
 * ‘-’ A "deleted" line in the first file that corresponds to nothing in the second file.
 * -------------------------------------------------------------------
 */

public class LessContextFormat implements IFormat {

    @Override
    public void format(IResultDiff resultDiff) {
        System.out.println("Less Context Format");
    }

    @Override
    public String getHeader(char format, int startLine1F, int finalLine1F, int startLine2F, int finalLine2F) {
        String addHeader = null;
        if (format == 'd') {
            addHeader = "--- " + Integer.toString(startLine1F);
            if (startLine1F != finalLine1F) {
                addHeader = addHeader + "," + finalLine1F;
            }
            addHeader = addHeader + "----";
        } else {
            addHeader = "*** " + Integer.toString(startLine2F);
            if (startLine2F != finalLine2F) {
                addHeader = addHeader + "," + finalLine1F;
            }
            addHeader = addHeader + "****";
        }
        return addHeader;
    }
}