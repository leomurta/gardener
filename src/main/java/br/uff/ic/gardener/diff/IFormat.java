package br.uff.ic.gardener.diff;

/**
 *
 * @author Daniel
 */
public interface IFormat {
    /**
     *
     * @param resultDiff
     */
    public void format(IResultDiff resultDiff);
    /**
     *
     * @param formatType
     * @param startLine1F
     * @param finalLine1F
     * @param startLine2F
     * @param finalLine2F
     * @return
     */
    public abstract String getHeader(char formatType, int startLine1F, int finalLine1F, int startLine2F, int finalLine2F);
}