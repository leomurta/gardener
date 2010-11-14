package br.uff.ic.gardener.diff;

import br.uff.ic.gardener.diff.LinesBean.Situation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ResultLCS implements IResultDiff {

    private List fileVersionOne;
    private List fileVersionTwo;
    private List lcs;
    private List result;

    public ResultLCS(List fileVersionOne, List fileVersionTwo, List lcs) {
        this.fileVersionOne = fileVersionOne;
        this.fileVersionTwo = fileVersionTwo;
        this.lcs = lcs;

        loadResult();
    }

    private void loadResult() {
        result = new ArrayList();
        int i = 0;
        int j = 0;
        Iterator itF1 = fileVersionOne.iterator();
        Iterator itF2 = fileVersionTwo.iterator();

        LinesBean lineF1 = (LinesBean) itF1.next();
        LinesBean lineF2 = (LinesBean) itF2.next();
        lineF1 = (LinesBean) itF1.next();
        lineF2 = (LinesBean) itF2.next();

        while (itF1.hasNext() && itF2.hasNext()) {
            if ((lineF1 != null) && (lineF2 != null)) {
                if ((lcs.contains(lineF1)) && (lcs.contains(lineF2))) {
                    setLinesBean(lineF1, i, LinesBean.Situation.UNCHANGED, 1);
                    setLinesBean(lineF2, j, LinesBean.Situation.UNCHANGED, 2);
                    lineF1 = (LinesBean) itF1.next();
                    lineF2 = (LinesBean) itF2.next();
                    i++;
                    j++;
                } else {
                    if (!lcs.contains(lineF1)) {
                        setLinesBean(lineF1, i, LinesBean.Situation.REMOVED, 1);
                        lineF1 = (LinesBean) itF1.next();
                        i++;
                    } else {
                        setLinesBean(lineF2, j, LinesBean.Situation.ADDED, 2);
                        lineF2 = (LinesBean) itF2.next();
                        j++;
                    }
                }
            }
        }
        while (itF1.hasNext()) {
            if (lineF1 != null) {
                if (!lcs.contains(lineF1)) {
                    setLinesBean(lineF1, i, LinesBean.Situation.ADDED, 1);
                } else {
                    setLinesBean(lineF1, i, LinesBean.Situation.UNCHANGED, 1);
                }
            }
            lineF1 = (LinesBean) itF1.next();
            i++;

        }
        setEndLine(lineF1, i, 1, Situation.REMOVED);

        while (itF2.hasNext()) {
            if (lineF2 != null) {
                if (!lcs.contains(lineF2)) {
                    setLinesBean(lineF2, j, LinesBean.Situation.ADDED, 2);
                } else {
                    setLinesBean(lineF2, j, LinesBean.Situation.UNCHANGED, 2);
                }
            }
            lineF2 = (LinesBean) itF2.next();
            j++;
        }
        setEndLine(lineF2, j, 2, Situation.ADDED);
    }

    public void setEndLine(LinesBean line, int id, int idFile, Situation situation) {
        if (line != null) {
            if ((line != null) && (!lcs.contains(line))) {
                setLinesBean(line, id, situation, idFile);
            } else {
                setLinesBean(line, id, LinesBean.Situation.UNCHANGED, idFile);
            }
        }
    }

    public void setLinesBean(LinesBean line, int id, Situation situation, int idFile) {
        line.setSituation(situation);
        if (idFile == 1) {
            line.setIndLineFileV1(id);
        } else {
            line.setIndLineFileV2(id);
        }
        result.add(line);
    }

    public List getFileVersionOne() {
        return fileVersionOne;
    }

    public void setFileVersionOne(List fileVersionOne) {
        this.fileVersionOne = fileVersionOne;
    }

    public List getFileVersionTwo() {
        return fileVersionTwo;
    }

    public void setFileVersionTwo(List fileVersionTwo) {
        this.fileVersionTwo = fileVersionTwo;
    }

    public List getLcs() {
        return lcs;
    }

    public void setLcs(List lcs) {
        this.lcs = lcs;
    }

    public List getResult() {
        return result;
    }
}
