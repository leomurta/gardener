/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.gardener.versioning;

import br.uff.ic.gardener.database.Database;
import br.uff.ic.gardener.server.*;

import java.io.File;

/**
 *
 * @author Evaldo de Oliveira
 */
public class ConfigurationItem {

    /**
    *
    * @param serv
    * @param vers
    * @param fileSource
    * @param fileName
    */
   public void createFileRevision(ConfigurationServer serv, Project proj, Revision vers, String fileSource, String fileName)
   {
       Database db = new Database();

       int revision = db.lastRevision(proj);

       vers.setRevisionNumber(Integer.toString(revision));

       db.storeFileRepository(vers, proj, fileSource, fileName);

   }

   /**
    *
    * @param serv
    * @param vers
    * @return
    */
   public File[] listRevisionFiles(Project proj, Revision vers)
   {

       Database db = new Database();
       return db.listFiles(proj, vers);

   }

}
