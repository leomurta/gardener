package br.uff.ic.gardener.diff;

import java.io.File;
import java.util.List;

public class Diff {

    private File fileVersionOne = null;
    private File fileVersionTwo = null;

    public Diff(File fileVersionOne, File fileVersionTwo) {
        this.fileVersionOne = fileVersionOne;
        this.fileVersionTwo = fileVersionTwo;
    }

    /**
     * Runs the diff according to the comparison algorithm
     * @return Object IResultDiff
     */
    public IResultDiff compare() {
        IDiff comparator = AlgorithmsFactory.getComparatorLcs();
        return comparator.diff(fileVersionOne, fileVersionTwo);
    }

    public void printResult(IResultDiff result) {
        List listResult = result.getResult();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //Files - test
        File file1 = new File("../lao.txt");
        File file2 = new File("../tzu.txt");

    }
}
