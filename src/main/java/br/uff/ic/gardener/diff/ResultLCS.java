package br.uff.ic.gardener.diff;

import br.uff.ic.gardener.diff.LinesBean.Situation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Fernanda
 */
public class ResultLCS implements IResultDiff {

    private List fileVersionOne;
    private List fileVersionTwo;
    private List lcs;
    private List result;

    /**
     *
     * @param fileVersionOne
     * @param fileVersionTwo
     * @param lcs
     */
    public ResultLCS(List fileVersionOne, List fileVersionTwo, List lcs) throws DiffException {
        this.fileVersionOne = fileVersionOne;
        this.fileVersionTwo = fileVersionTwo;
        this.lcs = lcs;

        loadResult();
    }

    private void loadResult() throws DiffException {
        result = new ArrayList();
        int i = 0;
        int j = 0;

        Iterator itF1 = fileVersionOne.iterator();
        Iterator itF2 = fileVersionTwo.iterator();

        LinesBean lineF1 = getNext(itF1);
        LinesBean lineF2 = getNext(itF2);

        while ((lineF1 != null) || (lineF2 != null)) {

            if (isContext(lineF1,lineF2)) {
                //Context
                setLinesBean(lineF1, i++, LinesBean.Situation.UNCHANGED, 1);
                lineF1 = getNext(itF1);

                setLinesBean(lineF2, j++, LinesBean.Situation.UNCHANGED, 2);
                lineF2 = getNext(itF2);
            } else if (isRemoving(lineF1,lineF2)) {
                //Removed
                setLinesBean(lineF1, i++, LinesBean.Situation.REMOVED, 1);
                lineF1 = getNext(itF1);
            } else if (isAdding(lineF1,lineF2)){
                //Addition
                setLinesBean(lineF2, j++, LinesBean.Situation.ADDED, 2);
                lineF2 = getNext(itF2);
            } else{
                throw new DiffException(DiffException.MSG_INVALIDOPTION);
            }
        }//while
    }

    private boolean isContext(LinesBean lineF1,LinesBean lineF2){
        if( (lineF1 == null) || (lineF2 == null)){
            return false;
        }
        return (lcs.contains(lineF1)) && (lcs.contains(lineF2));
    }

    private boolean isAdding(LinesBean lineF1,LinesBean lineF2){
        if( (lineF2 == null)){
            return false;
        }
        return !lcs.contains(lineF2);
    }

    private boolean isRemoving(LinesBean lineF1,LinesBean lineF2){
        if( (lineF1 == null)){
            return false;
        }
        return !lcs.contains(lineF1);
    }
    
    /**
     *
     * @param line
     * @param id
     * @param situation
     * @param idFile
     */
    public void setLinesBean(LinesBean line, int id, Situation situation, int idFile) {
        line.setSituation(situation);
        if (idFile == 1) {
            line.setIndLineFileV1(id);
        } else {
            line.setIndLineFileV2(id);
        }
        result.add(line);
    }

    /**
     *
     * @return
     */
    public List getFileVersionOne() {
        return fileVersionOne;
    }

    /**
     *
     * @param fileVersionOne
     */
    public void setFileVersionOne(List fileVersionOne) {
        this.fileVersionOne = fileVersionOne;
    }

    /**
     *
     * @return
     */
    public List getFileVersionTwo() {
        return fileVersionTwo;
    }

    /**
     *
     * @param fileVersionTwo
     */
    public void setFileVersionTwo(List fileVersionTwo) {
        this.fileVersionTwo = fileVersionTwo;
    }

    /**
     *
     * @return
     */
    public List getLcs() {
        return lcs;
    }

    /**
     *
     * @param lcs
     */
    public void setLcs(List lcs) {
        this.lcs = lcs;
    }

    /**
     *
     * @return
     */
    public List getResult() {
        return result;
    }

    private LinesBean getNext(Iterator it) {
        Object obj = null;
        if (it.hasNext()) {
            obj = it.next();
            if (obj == null) {
                return getNext(it);
            }
        }
        return (LinesBean) obj;
    }
}
