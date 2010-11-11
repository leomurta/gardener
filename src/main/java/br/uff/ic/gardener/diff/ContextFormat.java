package br.uff.ic.gardener.diff;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContextFormat implements IFormat {

    @Override
    public void format(IResultDiff resultDiff) {
        int sizeContext = 0;
        LinesBean line = null;
        List linesOutputF1 = new ArrayList();
        List linesOutputF2 = new ArrayList();

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
        outputWriter.println(FormatFactory.getMainHeader().split("\\!!")[0]);
        outputWriter.println(FormatFactory.getMainHeader().split("\\!!")[1]);
        outputWriter.println();

        for (Iterator it = resultDiff.getResult().iterator(); it.hasNext();) {
            hasLine = false;
            if (!isNext) {
                line = (LinesBean) it.next();
            }
            do {
                if (line.getSituation() == LinesBean.Situation.ADDED) {
                    if (sizeContext > 3) {
                        linesOutputF2.remove(0);
                        sizeContext--;
                    }
                    hasLineDeletedOrAdded = true;
                    linesOutputF2.add("+ " + line.getLine());
                    sizeContext = 0;
                    if (idInitialLine2 == -1) {
                        idInitialLine2 = line.getIndLineFileV2() + 1;
                    }
                    idFinalLine2 = line.getIndLineFileV2() + 1;
                } else {
                    if (line.getSituation() == LinesBean.Situation.REMOVED) {
                        if (sizeContext > 3) {
                            linesOutputF1.remove(0);
                            sizeContext--;
                            idInitialLine1 = -1;
                        }
                        hasLineDeletedOrAdded = true;
                        linesOutputF1.add("- " + line.getLine());
                        sizeContext = 0;
                        if (idInitialLine1 == -1) {
                            idInitialLine1 = line.getIndLineFileV1() + 1;
                        }
                        idFinalLine1 = line.getIndLineFileV1() + 1;
                    } else {
                        if (line.getSituation() == LinesBean.Situation.UNCHANGED) {
                            if (sizeContext == 3) {
                                linesOutputF1.remove(0);
                                linesOutputF2.remove(0);

                                sizeContext--;
                                idInitialLine1++;
                                idInitialLine2++;
                            }
                            sizeContext++;
                            linesOutputF1.add("  " + line.getLine());
                            linesOutputF2.add("  " + line.getLine());

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
                    linesOutputF2.add("+ " + line.getLine());
                    idFinalLine2++;
                } else {
                    if (line.getSituation() == LinesBean.Situation.REMOVED) {
                        linesOutputF1.add("- " + line.getLine());
                        idFinalLine1++;
                    }
                }
            }
            if (hasLineDeletedOrAdded) {
                outputWriter.println("***************");
                outputWriter.println(getHeader('d', idInitialLine1, idFinalLine1, idInitialLine2, idFinalLine2));
                if (canPrint(linesOutputF1, "-")) {
                    printOutput(linesOutputF1, outputWriter);
                }
                linesOutputF1.clear();

                outputWriter.println(getHeader('a', idInitialLine1, idFinalLine1, idInitialLine2, idFinalLine2));

                if (canPrint(linesOutputF2, "+")) {
                    printOutput(linesOutputF2, outputWriter);
                }
                linesOutputF2.clear();

                idInitialLine1 = -1;
                idInitialLine2 = -1;
                sizeContext = 0;
                hasLineDeletedOrAdded = false;
            }
        }

    }

    @Override
    public String getHeader(char format, int startLine1F, int finalLine1F, int startLine2F, int finalLine2F) {
        String addHeader = null;
        if (format == 'a') {
            addHeader = "--- " + Integer.toString(startLine2F);
            if (startLine2F != finalLine2F) {
                addHeader = addHeader + "," + finalLine2F;
            }
            addHeader = addHeader + "----";
        } else {
            addHeader = "*** " + Integer.toString(startLine1F);
            if (startLine1F != finalLine1F) {
                addHeader = addHeader + "," + finalLine1F;
            }
            addHeader = addHeader + "****";
        }
        return addHeader;
    }

    private void printOutput(List listLines, PrintWriter outputWriter) {
        for (int i = 0; i < listLines.size(); i++) {
            System.out.println(listLines.get(i));
            outputWriter.println(listLines.get(i));
        }
    }

    private boolean canPrint(List listLines, String idFormat) {
        boolean contain = false;
        for (int i = 0; i < listLines.size(); i++) {
            if (listLines.get(i).toString().contains(idFormat)) {
                contain = true;
                i=listLines.size();
            }
        }
        return contain;
    }
}