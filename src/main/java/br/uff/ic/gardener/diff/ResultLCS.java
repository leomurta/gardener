package br.uff.ic.gardener.diff;

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

        for (Iterator it = fileVersionOne.iterator(); it.hasNext(); i++) {
            LinesBean line = (LinesBean) it.next();
            if (!lcs.contains(line)) {
                line.setSituation(LinesBean.Situation.REMOVED);
                line.setIndLineFileV1(i);
                result.add(line);
            }
        }

        i = 0;

        for (Iterator it = fileVersionTwo.iterator(); it.hasNext(); i++) {
            LinesBean line = (LinesBean) it.next();
            if (!lcs.contains(line)) {
                line.setSituation(LinesBean.Situation.ADDED);
                line.setIndLineFileV2(i);
                result.add(line);
            }
        }
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
