/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.gardener.server;

import br.uff.ic.gardener.versioning.*;

import java.util.*;
import java.io.File;

/**
 *
 * @author Evaldo de Oliveira
 */
public class Server {

    /**
    *
    * @param serv
    * @param vers
    * @param fileSource
    * @param fileName
    */
   public void ckeckin(ConfigurationServer serv
                     , Revision vers
                     , Project proj
                     , String fileSource
                     , String fileName)
    {

        Transaction transact = new Transaction();
        ConfigurationItem ci = new ConfigurationItem();

        transact.createRevision(serv, proj, vers);
        ci.createFileRevision(serv, proj, vers, fileSource, fileName);
    }

   /**
    *
    * @param serv
    * @param revisionNumber
    * @return
    */
   public ArrayList ckeckout(ConfigurationServer serv, Project proj, String revisionNumber)
   {

       Transaction trans = new Transaction();
       Revision revision = new Revision();

       revision.setRevisionNumber(revisionNumber);

       return trans.metadataRevision(serv, proj, revision);

   }

   /**
    *
    * @param serv
    * @param revisionNumber
    * @return
    */
   public File[] ckeckoutFile(Project proj, String revisionNumber)
   {

       Revision revision = new Revision();
       ConfigurationItem ci = new ConfigurationItem();

       revision.setRevisionNumber(revisionNumber);

       return ci.listRevisionFiles(proj, revision);

   }

   /**
    *
    * @param serv
    * @return
    */
   public int nextNumberRevision(Project proj)
   {

       Transaction trans = new Transaction();
       return trans.nextRevision(proj);

   }

}
