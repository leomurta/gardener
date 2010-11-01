/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.gardener.versioning;

import br.uff.ic.gardener.database.Database;
import br.uff.ic.gardener.server.*;
//--

import java.util.*;

/**
 *
 * @author Evaldo de Oliveira
 */
public class Transaction {
    /**
    *
    * @param serv
    * @param vers
    */
   public void createRevision(ConfigurationServer serv, Project proj, Revision vers)
    {

       Database db = new Database();

       int revision = db.lastRevision(proj);
       revision = revision + 1;

       vers.setRevisionNumber(Integer.toString(revision));

       db.storeDirRepository(serv, proj, vers);
       db.updateRevision(proj, vers);
     }   

   /**
    *
    * @param serv
    * @return
    */
   public int lastRevision(Project proj)
   {

       Database db = new Database();
       return db.lastRevision(proj);

   }

    /**
    *
    * @param serv
    * @return
    */
   public int nextRevision(Project proj)
   {

       Database db = new Database();
       return db.lastRevision(proj) + 1;

   }

   /**
    *
    * @param serv
    * @param vers
    * @return
    */
   public ArrayList metadataRevision(ConfigurationServer serv, Project proj, Revision vers)
   {

       Database db = new Database();
       return db.queryMetadata(serv, proj, vers);

   }


}
