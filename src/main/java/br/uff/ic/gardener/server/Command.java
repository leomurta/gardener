package padraocommand;

//interface Command

import java.io.File;

/**
 *
 * @author Evaldo de Oliveira
 */
public abstract class Command {
        
    /**
     *
     * @param project
     */
    public void execute(String project)
        {

        };

        /**
         *
         * @param project
         * @param user
         */
        public void execute(String project, String user)
        {

        };

        /**
         *
         * @param project
         * @param user
         * @param date
         * @param message
         * @param path
         * @param itens
         */
        public void execute(String project
                      , String user
                      , String date
                      , String message
                      , String path
                      , File[] itens){

        };

        /**
         *
         * @param project
         */
        public void commit(String project)
        {

        };        
        

        /**
         *
         * @param project
         */
        public void unExecute(String project)
        {

        };       
	
        
}
