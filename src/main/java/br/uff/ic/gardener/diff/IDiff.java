package br.uff.ic.gardener.diff;

import java.io.File;

public interface IDiff {

    public IResultDiff diff(File fileVersionOne, File fileVersionTwo);
}
