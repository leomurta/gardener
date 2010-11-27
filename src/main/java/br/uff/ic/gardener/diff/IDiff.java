package br.uff.ic.gardener.diff;

import java.io.File;

/**
 *
 * @author Fernanda
 */
public interface IDiff {

    /**
     *
     * @param fileVersionOne
     * @param fileVersionTwo
     * @return
     */
    public IResultDiff diff(File fileVersionOne, File fileVersionTwo);
}
