package br.uff.ic.gardener.diff;

import br.uff.ic.gardener.diff.LCSBean.Arrow;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class LCS implements IDiff {

    private LCSBean[][] arrayLcs;
    private List<LinesBean> linesFileOne = new ArrayList<LinesBean>();
    private List<LinesBean> columnFileTwo = new ArrayList<LinesBean>();

    /**
     *
     */
    public LCS() {
    }
    /**
     *
     * @param linesFileOne
     * @param columnFileTwo
     * @param arrayLCS
     */
    public LCS(List<LinesBean> linesFileOne, List<LinesBean> columnFileTwo, LCSBean[][] arrayLCS) {
        this.linesFileOne = linesFileOne;
        this.columnFileTwo = columnFileTwo;
        this.arrayLcs = arrayLCS;

    }
    /**
     *
     * @param fileVersionOne
     * @param fileVersionTwo
     * @return
     */
    public IResultDiff diff(File fileVersionOne, File fileVersionTwo) {
        try {
            start(fileVersionOne, fileVersionTwo);
        } catch (IOException ex) {
            Logger.getLogger(LCS.class.getName()).log(Level.SEVERE, null, ex);
        }

        lcs();

        IResultDiff result = printLCS();

        return result;
    }

    /**
     *
     * @param baseFile
     * @param comparedFile
     * @throws IOException
     */
    public void start(File baseFile, File comparedFile) throws IOException {
        String line = null;
        linesFileOne.add(null);
        columnFileTwo.add(null);
        BufferedReader reader = new BufferedReader(new FileReader(baseFile));
        while ((line = reader.readLine()) != null) {
            linesFileOne.add(new LinesBean(line));
        }

        line = null;
        reader = new BufferedReader(new FileReader(comparedFile));
        while ((line = reader.readLine()) != null) {
            columnFileTwo.add(new LinesBean(line));
        }
        arrayLcs = new LCSBean[linesFileOne.size()][columnFileTwo.size()];
    }

    /**
     *
     * @return
     */
    public boolean lcs() {

        int m = linesFileOne.size();
        int n = columnFileTwo.size();

        for (int i = 0; i < m; i++) {
            arrayLcs[i][0] = new LCSBean(0,Arrow.UP);
        }

        for (int j = 0; j < n; j++) {
            arrayLcs[0][j] = new LCSBean(0,Arrow.UP);
        }

        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (compare(linesFileOne.get(i), columnFileTwo.get(j))) {
                    arrayLcs[i][j] = new LCSBean(arrayLcs[i - 1][j - 1].getCounter() + 1, Arrow.DIAGONAL);
                } else {
                    if (arrayLcs[i - 1][j].getCounter() >= arrayLcs[i][j - 1].getCounter()) {
                        arrayLcs[i][j] = new LCSBean(arrayLcs[i - 1][j].getCounter(), Arrow.UP);
                    } else {
                        arrayLcs[i][j] = new LCSBean(arrayLcs[i][j - 1].getCounter(), Arrow.LEFT);
                    }
                }
            }
        }
        return true;
    }

    private boolean compare(LinesBean lineOne, LinesBean lineTwo) {
        return lineOne.getLine().compareTo(lineTwo.getLine()) == 0;
    }

    /**
     *
     * @return
     */
    public IResultDiff printLCS() {
        List lcs = new ArrayList();

        printLCS(lcs, linesFileOne.size()-1, columnFileTwo.size()-1);

        return new ResultLCS(linesFileOne, columnFileTwo, lcs);
    }

    private void printLCS(List lcs, int i, int j) {
        if (i == 0 || j == 0) {
            return;
        }
        if (arrayLcs[i][j].getArrow() == Arrow.DIAGONAL) {
            printLCS(lcs, i - 1, j - 1);
            //print xi - Validation.
            //System.out.println(linesFileOne.get(i).getLine());
            lcs.add(linesFileOne.get(i));
        } else {
            if (arrayLcs[i][j].getArrow() == Arrow.UP) {
                printLCS(lcs, i - 1, j);
            } else {
                printLCS(lcs, i, j - 1);
            }
        }
    }
}
