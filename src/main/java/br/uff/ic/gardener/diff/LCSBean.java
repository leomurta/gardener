package br.uff.ic.gardener.diff;

/**
 *
 * @author Daniel
 */
public class LCSBean {

    /**
     *
     */
    public enum Arrow {

        /**
         *
         */
        LEFT,
        /**
         * 
         */
        UP,
        /**
         *
         */
        DIAGONAL
    }
    private int counter = 0;
    private Arrow arrow;

    /**
     *
     */
    public LCSBean() {
    }

    /**
     *
     * @param counter
     * @param arrow
     */
    public LCSBean(int counter, Arrow arrow) {
        this.counter = counter;
        this.arrow = arrow;
    }

    /**
     *
     * @return
     */
    public int getCounter() {
        return counter;
    }

    /**
     *
     * @param counter
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }

    /**
     *
     * @return
     */
    public Arrow getArrow() {
        return arrow;
    }

    /**
     *
     * @param arrow
     */
    public void setArrow(Arrow arrow) {
        this.arrow = arrow;
    }
}
