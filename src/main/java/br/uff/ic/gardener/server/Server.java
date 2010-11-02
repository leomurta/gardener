/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.gardener.server;

import br.uff.ic.gardener.database.Database;
import br.uff.ic.gardener.versioning.*;
import java.io.File;

/**
 *
 * @author Evaldo de Oliveira, Alessandreia e Fernanda
 */
public class Server {

   private static Server instance = new Server();

   private Server() {

   }

   /**
    *
    * @return
    */
   public static Server getInstance() {
      return instance;
   }

   /**
    *
    * @param project
    * @param user
    */
   public void init(String project, String user)
    {
        Init in = new Init();
        in.execute(project, user);
    }

   /**
    *
    * @param project
    */
   public void commitInit(String project)
    {
        Init in = new Init();
        in.commit(project);
    }

    /**
     *
     * @param project
     */
    public void rollbackInit(String project)
    {
        Init in = new Init();
        in.unExecute(project);
    }

    /**
     *
     * @param project
     * @param user
     * @param date
     * @param message
     * @param path
     * @param itens
     */
    public void ckeckIn(String project
                      , String user
                      , String date
                      , String message
                      , String path
                      , File[] itens){

        Command ci = new Checkin();
        ci.execute(project, user, date, message, path, itens);

    }    

    /**
     *
     * @param project
     */
    public void commitCheckin(String project)
    {

        Checkin ci = new Checkin();
        ci.commit(project);

    }

    /**
     *
     * @param project
     */
    public void roolbackCheckin(String project)
    {

        Checkin ci = new Checkin();
        ci.unExecute(project);

    }

}
