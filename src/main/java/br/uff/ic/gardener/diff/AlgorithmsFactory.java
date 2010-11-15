package br.uff.ic.gardener.diff;

import java.io.File;

public class AlgorithmsFactory {

    static IDiff getComparator(File fileVersionOne, File fileVersionTwo) {
        if (fileVersionOne.isDirectory() && fileVersionTwo.isDirectory()) {
            return new DirectoryComparator();
        } else {
            if (fileVersionOne.isFile() && fileVersionTwo.isFile()) {
                return new LCS();
            } else {
                return new BinaryComparator();
            }
        }

    }
}