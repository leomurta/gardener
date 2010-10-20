package br.uff.ic.gardener.diff;

public class AlgorithmsFactory {

    private char idComparator = 'L';

    public static IDiff getComparatorLcs() {
        return new LCS();
    }

}
