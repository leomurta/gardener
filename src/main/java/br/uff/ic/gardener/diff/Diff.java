package br.uff.ic.gardener.diff;
import java.io.File;
import java.util.List;

/**
 *
 * @author Fernanda
 */
public class Diff {

    private final File fileVersionOne;
    private final File fileVersionTwo;
    private final char format;

    /**
     *
     * @param fileVersionOne
     * @param fileVersionTwo
     * @param format
     */
    public Diff(File fileVersionOne, File fileVersionTwo, char format) {
        this.fileVersionOne = fileVersionOne;
        this.fileVersionTwo = fileVersionTwo;
        this.format = format;
    }

    /**
     * Runs the diff according to the comparison algorithm
     * @return Object IResultDiff
     */
    public IResultDiff compare() {
        IDiff comparator = AlgorithmsFactory.getComparator(fileVersionOne, fileVersionTwo);
        return comparator.diff(fileVersionOne, fileVersionTwo);
    }

    /**
     *
     * @param resultDiff
     * @param formatName
     */
    public void createOutputFormat(IResultDiff resultDiff, char formatName) {
        IFormat formatter = FormatFactory.getFormatter(formatName);
        FormatFactory.setMainHeader(fileVersionOne, fileVersionTwo);
        formatter.format(resultDiff);
    }

    /**
     *
     * @param result
     */
    public void printResult(IResultDiff result) {
        List listResult = result.getResult();
    }

    /**
     *
     */
    public void setOutputFormat() {
        IResultDiff resultDiff = this.compare();
        this.createOutputFormat(resultDiff, format);
    }
}
