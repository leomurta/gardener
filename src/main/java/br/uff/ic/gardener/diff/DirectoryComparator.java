package br.uff.ic.gardener.diff;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class DirectoryComparator implements IDiff{

    private LCSBean[][] arrayLcs;
    private List<LinesBean> linesFileOne = new ArrayList<LinesBean>();
    private List<LinesBean> columnFileTwo = new ArrayList<LinesBean>();
    private List<LinesBean> lines = new ArrayList<LinesBean>();

    public DirectoryComparator() {
    }

    private List<LinesBean> getChildrens(File fileVersion) {
        File[] children = fileVersion.listFiles();
        for (int i = 0; i < children.length; i++) {
            String pathFileVersion = fileVersion.getAbsolutePath().toString();
            String pathChildren = children[i].getAbsolutePath().toString();
            lines.add(new LinesBean(pathChildren.substring(pathFileVersion.length())));
            if (children[i].isDirectory()) {
                lines = getChildrens(children[i]);
            }
        }
        return lines;
    }

    public IResultDiff diff(File fileVersionOne, File fileVersionTwo) {
        try {
            start(fileVersionOne, fileVersionTwo);
        } catch (IOException ex) {
            Logger.getLogger(LCS.class.getName()).log(Level.SEVERE, null, ex);
        }
        LCS algorithm = new LCS(linesFileOne, columnFileTwo,arrayLcs);

        algorithm.lcs();

        IResultDiff result = algorithm.printLCS();

        return result;
    }

    public void start(File baseFile, File comparedFile) throws IOException {
        linesFileOne.add(null);
        columnFileTwo.add(null);

        linesFileOne.addAll(getChildrens(baseFile));
        lines.clear();
        columnFileTwo.addAll(getChildrens(comparedFile));

        arrayLcs = new LCSBean[linesFileOne.size()][columnFileTwo.size()];
    }

}