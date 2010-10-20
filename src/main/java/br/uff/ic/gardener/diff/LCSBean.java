package br.uff.ic.gardener.diff;

public class LCSBean {

    public enum Arrow {

        LEFT, UP, DIAGONAL
    }
    private int counter = 0;
    private Arrow arrow;

    public LCSBean() {
    }

    public LCSBean(int counter, Arrow arrow) {
        this.counter = counter;
        this.arrow = arrow;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public Arrow getArrow() {
        return arrow;
    }

    public void setArrow(Arrow arrow) {
        this.arrow = arrow;
    }
}
