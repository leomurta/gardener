/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package padraocommand;

/**
 *
 * @author Evaldo de Oliveira, Alessandreia e Fernanda
 */
public class Checkout extends Command {

    private int state;
    /**
     *
     * @param project
     */
    @Override

        public void execute(String project)
        {

            System.out.println("Checkout realizado com sucesso.");

        }

        public void setState(int state)
        {
            this.state = state;
        }

        public int getState()
        {
            return this.state;
        }

}
