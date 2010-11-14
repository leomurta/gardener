package br.uff.ic.gardener.diff;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FullContextFormat implements IFormat {
    /*
     * Format for exclusive use of merge module
     */

    @Override
    public void format(IResultDiff resultDiff) {

        PrintWriter outputWriter = null;
        try {
            outputWriter = WriterFactory.getWriter();
        } catch (IOException ex) {
            Logger.getLogger(NormalFormat.class.getName()).log(Level.SEVERE, null, ex);
        }

        outputWriter.println(FormatFactory.getMainHeader().split("\\!!")[0].replaceAll("\\*\\*\\*", "---"));
        outputWriter.println(FormatFactory.getMainHeader().split("\\!!")[1].replaceAll("---", "+++"));
        outputWriter.println();

        for (Iterator it = resultDiff.getResult().iterator(); it.hasNext();) {
            LinesBean line = (LinesBean) it.next();

            if (line.getSituation() == LinesBean.Situation.REMOVED) {
                outputWriter.println("- " + line.getLine());
            } else if (line.getSituation() == LinesBean.Situation.ADDED) {
                outputWriter.println("+ " + line.getLine());
            } else {
                outputWriter.println("  " + line.getLine());
                line = (LinesBean) it.next();
            }
        }
        outputWriter.close();
    }

    @Override
    public String getHeader(char formatType, int startLine1F, int finalLine1F, int startLine2F, int finalLine2F) {
        return null;


    }
}
