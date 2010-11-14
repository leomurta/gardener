package br.uff.ic.gardener.diff;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NormalFormat implements IFormat {

    public NormalFormat() {
    }
    /* -------------- DETAILS DESCRIPTION OF NORMAL FORMAT --------------
     * lar - add the lines range r f the second file after line l
     *       of the first file.
     * fct - replace the lines in range f of the first file with
     *       lines in range t of the second file
     * rdl - delete the lines in range r from de the first file;
     *       line l is where they would have appeared in the second file
     *       had they not been deleted
     * -------------------------------------------------------------------
     */

    @Override
    public void format(IResultDiff resultDiff) {
        List listLines = new ArrayList();
        int startF1 = 0, endF1 = 0, startF2 = 0, endF2 = 0;
        boolean isNext = false;

        PrintWriter outputWriter = null;
        try {
            outputWriter = WriterFactory.getWriter();
        } catch (IOException ex) {
            Logger.getLogger(NormalFormat.class.getName()).log(Level.SEVERE, null, ex);
        }

        outputWriter.println(FormatFactory.getMainHeader().split("\\!!")[0]);
        outputWriter.println(FormatFactory.getMainHeader().split("\\!!")[1]);
        outputWriter.println();

        LinesBean line = null;

        for (Iterator it = resultDiff.getResult().iterator(); it.hasNext();) {
            if (!isNext) {
                line = (LinesBean) it.next();
            }
            isNext = false;
            listLines.clear();

            if (line.getSituation() == LinesBean.Situation.REMOVED) {
                startF1 = line.getIndLineFileV1() + 1;
                endF1 = line.getIndLineFileV1() + 1;

                while ((line.getSituation() == LinesBean.Situation.REMOVED) && (it.hasNext())) {
                    listLines.add("< " + line.getLine());
                    endF1 = line.getIndLineFileV1() + 1;
                    line = (LinesBean) it.next();
                    isNext = true;
                }

                outputWriter.println(getHeader('d', startF1, endF1, startF2, endF2));
                printOutput(listLines, outputWriter);
            }
            isNext = false;
            listLines.clear();
            if (line.getSituation() == LinesBean.Situation.ADDED) {
                startF2 = line.getIndLineFileV2() + 1;
                endF2 = line.getIndLineFileV2() + 1;

                while ((line.getSituation() == LinesBean.Situation.ADDED) && (it.hasNext())) {
                    listLines.add("> " + line.getLine());
                    endF2 = line.getIndLineFileV2() + 1;
                    line = (LinesBean) it.next();
                    isNext = true;
                }

                if (!it.hasNext() && line.getSituation() != LinesBean.Situation.UNCHANGED) {
                    listLines.add("> " + line.getLine());
                    endF2 = line.getIndLineFileV2() + 1;
                }
                outputWriter.println(getHeader('a', startF1, endF1, startF2, endF2));
                printOutput(listLines, outputWriter);
            }
            if (line.getSituation() == LinesBean.Situation.UNCHANGED) {
                if (line.getIndLineFileV1() != -1) {
                    startF1 = line.getIndLineFileV1() + 1;
                    endF1 = line.getIndLineFileV1() + 1;
                }
                if (line.getIndLineFileV2() != -1) {
                    startF2 = line.getIndLineFileV2() + 1;
                    endF2 = line.getIndLineFileV2() + 1;
                }
                //line = (LinesBean) it.next();
            }
        }
        outputWriter.close();
    }

    @Override
    public String getHeader(char format, int startLine1F, int finalLine1F, int startLine2F, int finalLine2F) {
        String addHeader = Integer.toString(startLine1F);
        if (startLine1F != finalLine1F) {
            addHeader = addHeader + "," + finalLine1F;
        }
        addHeader = addHeader + format + Integer.toString(startLine2F);
        if (startLine2F != finalLine2F) {
            addHeader = addHeader + "," + finalLine2F;
        }
        return addHeader;
    }

    private void printOutput(List listLines, PrintWriter outputWriter) {
        for (int i = 0; i < listLines.size(); i++) {
            System.out.println(listLines.get(i));
            outputWriter.println(listLines.get(i));

        }
    }
}
