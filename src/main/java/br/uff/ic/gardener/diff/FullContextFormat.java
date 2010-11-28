package br.uff.ic.gardener.diff;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Fernanda
 */
public class FullContextFormat implements IFormat {
    /*
     * Format for exclusive use of merge module
     */

    /**
     *
     * @param resultDiff
     */
    @Override
    public void format(IResultDiff resultDiff) {
        LinesBean line = null;
        boolean isNext = false;

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
            if (!isNext) {
                line = (LinesBean) it.next();
                isNext = false; 
             }

           if (line.getSituation() == LinesBean.Situation.REMOVED) {
                outputWriter.println("- " + line.getLine());
            } else if (line.getSituation() == LinesBean.Situation.ADDED) {
                outputWriter.println("+ " + line.getLine());
            } else {
                outputWriter.println("  " + line.getLine());

                //context lines are replicated, so jump extra one
                if(it.hasNext()){
                    it.next();
                    isNext = true;
                }
            }
        }
        outputWriter.close();
    }

    /**
     *
     * @param formatType
     * @param startLine1F
     * @param finalLine1F
     * @param startLine2F
     * @param finalLine2F
     * @return
     */
    @Override
    public String getHeader(char formatType, int startLine1F, int finalLine1F, int startLine2F, int finalLine2F) {
        return null;

    }
}