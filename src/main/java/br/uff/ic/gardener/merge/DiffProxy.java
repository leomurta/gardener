package br.uff.ic.gardener.merge;

import br.uff.ic.gardener.diff.DiffException;
import java.io.File;

import br.uff.ic.gardener.diff.Diff;
import br.uff.ic.gardener.diff.WriterFactory;

public class DiffProxy {

    public static File diff(File file1, File file2) throws DiffException {
        String fileOutName = "diff_" + file1.getName() + "_" + file2.getName() + "_toBeMerged";

        WriterFactory.setWriter(fileOutName + ".txt");
        Diff diff = new Diff(file1, file2, 'f');
        diff.setOutputFormat();

        return new File(fileOutName + ".txt");
    }
}
