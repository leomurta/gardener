package br.uff.ic.gardener.diff;

public class UnifiedFormat implements IFormat {

    /* -------------- DETAILS DESCRIPTION OF UNIFIED FORMAT --------------
     * --- from-file from-file-modification-time
     * +++ to-file to-file-modification-time
     * Unified format hunks look like this:
     * @@ from-file-line-numbers to-file-line-numbers @@
     * line-from-either-file
     * line-from-either-file...
     * If a hunk and its context contain two or more lines, its line numbers look
     * like ‘start,count’
     * An empty hunk is considered to end at the line that precedes the hunk.
     * The lines common to both files begin with a space character.
     * '+’ A line was added here to the first file.
     * ‘-’ A line was removed here from the first file.
     * -------------------------------------------------------------------
     */
    @Override
    public void format(IResultDiff resultDiff) {
        System.out.println("Unified Format");
    }

    @Override
    public String getHeader(char formatType, int startLine1F, int finalLine1F, int startLine2F, int finalLine2F) {
        String addHeader = "@@ -";
        //account number of lines
        addHeader = addHeader + Integer.toString(startLine1F) + "," + Integer.toString(finalLine1F - startLine1F + 1);
        addHeader = addHeader + " +";
        addHeader = addHeader + Integer.toString(startLine2F) + "," + Integer.toString(finalLine2F - startLine2F + 1);
        return addHeader + " @@";
    }
}
