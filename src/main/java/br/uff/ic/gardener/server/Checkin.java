/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package padraocommand;

import java.io.File;

//classe OpenCommand
/**
 *
 * @author Evaldo de Oliveira
 */
public class Checkin extends Command {
    
    private int state;
    /**
     *
     * @param project
     * @param user
     * @param date
     * @param message
     * @param path
     * @param itens
     */
    @Override
	public void execute(String project
                      , String user
                      , String date
                      , String message
                      , String path
                      , File[] itens){        

             LogCommandServer logCommand = LogCommandServer.getInstance();

             if (logCommand.commandOutStanding(this.getClass().getSimpleName(), project).equals("FALSE"))
             {
                 logCommand.addLog(this.getClass().getSimpleName(), user, project);
                 logCommand.addOutStanding(this.getClass().getSimpleName(), project, user, date, message, path, itens);

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
            logCommand.regOutStanding(this.getClass().getSimpleName(), project);

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
