package br.uff.ic.gardener.diff;

import java.io.File;

public class BinaryComparator implements IDiff {
    public BinaryComparator() {
    }

    @Override
    public IResultDiff diff(File fileVersionOne, File fileVersionTwo) throws DiffException{
        throw new UnsupportedOperationException("Not supported yet.");
    }

}