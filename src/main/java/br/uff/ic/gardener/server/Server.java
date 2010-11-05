/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.gardener.server;

import br.uff.ic.gardener.versioning.*;
import java.util.*;

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
   public String init(String project, String user)
    {
        Init in = new Init();
        in.execute(project, user);

        return this.commitInit(project);

    }

   /**
    *
    * @param project
    */
   public String commitInit(String project)
    {
       try{
        Init in = new Init();
        in.commit(project);

        return "Project " + project + " was created successful.";

       }catch(Exception err){

           Init roll = new Init();
           roll.unExecute(project);

           return err.getMessage();
       }
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
    public String ckeckIn(String project
                      , String user
                      , String date
                      , String message
                      , String path
                      , ArrayList itens){

        Checkin ck = new Checkin();
        ck.execute(project, user, date, message, path, itens);
        
        return this.commitCheckin(project);

    }    

    /**
     *
     * @param project
     */
    public String commitCheckin(String project)
    {
        
        try
        {
            Version vers = new Version();
            Checkin ck = new Checkin();

            ck.commit(project);

            return Integer.toString(Integer.parseInt(vers.getCurrentVersionProject(project))+1);

       }catch(Exception err){

           Checkin ck = new Checkin();
           ck.unExecute(project);

           return err.getMessage();
       }

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

    public long getLastRevision(String project)
    {
    	return 0;
    }

}
