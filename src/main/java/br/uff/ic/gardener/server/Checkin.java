/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.gardener.server;

import br.uff.ic.gardener.versioning.*;
import java.util.*;

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
                      , ArrayList itens){

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

            ArrayList outStanding;
            ArrayList outFileStanding;

            ConfigurationItem ci = new ConfigurationItem();
            Version vers = new Version();

            LogCommandServer logCommand = LogCommandServer.getInstance();

            this.setState(2);

            logCommand.updateLog(this.getClass().getSimpleName(),project, this.getState());
            outStanding = logCommand.regOutStanding(this.getClass().getSimpleName(), project);
            outFileStanding = logCommand.regFileOutStanding(this.getClass().getSimpleName(), project);

            int currentVersion = Integer.parseInt(vers.getCurrentVersionProject(project)) + 1;
            int nextVersion    = currentVersion + 1;

            for (int i = 0; i < outFileStanding.size(); i++)
            {

                ci.createConfigurationItem(
                        project
                      , currentVersion
                      , nextVersion
                      , outStanding.get(0).toString()
                      , outStanding.get(1).toString()
                      , outStanding.get(2).toString()
                      , outStanding.get(3).toString()
                      , outFileStanding.get(i).toString());
            }

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
