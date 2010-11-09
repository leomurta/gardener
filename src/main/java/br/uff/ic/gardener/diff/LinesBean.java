package br.uff.ic.gardener.diff;

public class LinesBean {

    private String line;
    private Situation situation = Situation.UNCHANGED;
    private int indLineFileV1 = -1;
    private int indLineFileV2 = -1;

    public enum Situation {

        UNCHANGED, CHANGED, ADDED, REMOVED
    }

    public LinesBean(String line) {
        this.line = line;
    }

    @Override
    public boolean equals(Object o) {
        return ((this.line.compareTo(((LinesBean) o).line)) == 0);
    }

    public LinesBean(String line, Situation situation) {
        this.line = line;
        this.situation = situation;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public Situation getSituation() {
        return situation;
    }

    public void setSituation(Situation situation) {
        this.situation = situation;
    }

    public int getIndLineFileV1() {
        return indLineFileV1;
    }

    public void setIndLineFileV1(int indLineFileV1) {
        this.indLineFileV1 = indLineFileV1;
    }

    public int getIndLineFileV2() {
        return indLineFileV2;
    }

    public void setIndLineFileV2(int indLineFileV2) {
        this.indLineFileV2 = indLineFileV2;
    }
}
