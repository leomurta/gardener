package br.uff.ic.gardener.diff;

public interface IFormat {
    public void format(IResultDiff resultDiff);
    public abstract String getHeader(char formatType, int startLine1F, int finalLine1F, int startLine2F, int finalLine2F);
}