/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.gardener.server;

import br.uff.ic.gardener.versioning.*;


/**
 *
 * @author Evaldo de Oliveira, Alessandreia e Fernanda
 */
public class Init extends Command {

    private int state;
    /**
     *
     * @param project
     * @param user
     */
    @Override
	public void execute(String project, String user)
        {        
             LogCommandServer logCommand = LogCommandServer.getInstance();

             if (logCommand.commandOutStanding(this.getClass().getSimpleName(), project).equals("FALSE"))
             {
                 logCommand.addLog(this.getClass().getSimpleName(), user, project);
                 logCommand.addOutStanding(this.getClass().getSimpleName(), project, " ", " ", " ", " ", null);

                 this.setState(1);

             }

	}

    /**
     *
     * @param project
     */
    @Override
        public void unExecute(String project)
        {
            LogCommandServer logComm = LogCommandServer.getInstance();

            this.setState(3);

            logComm.updateLog(this.getClass().getSimpleName(), project, this.getState());
        }

    /**
     *
     * @param project
     */
    @Override
        public void commit(String project)
        {
            LogCommandServer logCommand = LogCommandServer.getInstance();

            this.setState(2);

            logCommand.updateLog(this.getClass().getSimpleName(),project, this.getState());

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
