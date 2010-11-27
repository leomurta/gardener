package br.uff.ic.gardener.diff;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class UnifiedFormat implements IFormat {

    /**
     *
     * @param resultDiff
     */
    @Override
    public void format(IResultDiff resultDiff) {
        int sizeContext = 0;
        LinesBean line = null;
        List linesOutput = new ArrayList();
        boolean hasLineDeletedOrAdded = false;
        int idInitialLine1 = -1, idInitialLine2 = -1, idFinalLine1 = -1, idFinalLine2 = -1;
        boolean isNext = false;
        boolean hasLine = false;

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
            hasLine = false;
            if (!isNext) {
                line = (LinesBean) it.next();
            }
            do {
                if (line.getSituation() == LinesBean.Situation.ADDED) {
                    if (sizeContext > 3) {
                        linesOutput.remove(0);
                        sizeContext--;
                    }
                    hasLineDeletedOrAdded = true;
                    linesOutput.add("+ " + line.getLine());
                    sizeContext = 0;
                    if (idInitialLine2 == -1) {
                        idInitialLine2 = line.getIndLineFileV2() + 1;
                    }
                    idFinalLine2 = line.getIndLineFileV2() + 1;
                } else {
                    if (line.getSituation() == LinesBean.Situation.REMOVED) {
                        if (sizeContext > 3) {
                            linesOutput.remove(0);
                            sizeContext--;
                            idInitialLine1 = -1;
                        }
                        hasLineDeletedOrAdded = true;
                        linesOutput.add("- " + line.getLine());
                        sizeContext = 0;
                        if (idInitialLine1 == -1) {
                            idInitialLine1 = line.getIndLineFileV1() + 1;
                        }
                        idFinalLine1 = line.getIndLineFileV1() + 1;
                    } else {
                        if (line.getSituation() == LinesBean.Situation.UNCHANGED) {
                            if (sizeContext == 3) {
                                linesOutput.remove(0);
                                sizeContext--;
                                idInitialLine1++;
                                idInitialLine2++;
                            }
                            sizeContext++;
                            linesOutput.add("  " + line.getLine());
                            if (idInitialLine1 == -1) {
                                idInitialLine1 = line.getIndLineFileV1() + 1;
                                idFinalLine1 = line.getIndLineFileV1() + 1;
                            } else {
                                idFinalLine1++;
                            }

                            line = (LinesBean) it.next();
                            if (idInitialLine2 == -1) {
                                idInitialLine2 = line.getIndLineFileV2() + 1;
                                idFinalLine2 = line.getIndLineFileV2() + 1;
                            } else {
                                idFinalLine2++;
                            }
                        }
                    }
                }
                if (it.hasNext()) {
                    line = (LinesBean) it.next();
                    isNext = true;
                }

            } while ((sizeContext < 3) && (it.hasNext()));
            if ((!it.hasNext()) && (!line.getLine().isEmpty())) {
                if (line.getSituation() == LinesBean.Situation.ADDED) {
                    linesOutput.add("+ " + line.getLine());
                    idFinalLine2++;
                } else {
                    if (line.getSituation() == LinesBean.Situation.REMOVED) {
                        linesOutput.add("- " + line.getLine());
                        idFinalLine1++;
                    }
                }
            }
            if (hasLineDeletedOrAdded) {

                outputWriter.println(getHeader('u', idInitialLine1, idFinalLine1, idInitialLine2, idFinalLine2));
                printOutput(linesOutput, outputWriter);
                linesOutput.clear();
                idInitialLine1 = -1;
                idInitialLine2 = -1;
                sizeContext = 0;
                hasLineDeletedOrAdded = false;
            }
        }

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
    public String getHeader(
            char formatType, int startLine1F, int finalLine1F, int startLine2F, int finalLine2F) {
        String addHeader = "@@ -";
        //account number of lines
        addHeader = addHeader + Integer.toString(startLine1F) + "," + Integer.toString(finalLine1F - startLine1F + 1);
        addHeader = addHeader + " +";
        addHeader = addHeader + Integer.toString(startLine2F) + "," + Integer.toString(finalLine2F - startLine2F + 1);

        return addHeader + " @@";
    }

    private void printOutput(List listLines, PrintWriter outputWriter) {
        for (int i = 0; i <
                listLines.size(); i++) {
            System.out.println(listLines.get(i));
            outputWriter.println(listLines.get(i));
        }
    }
}