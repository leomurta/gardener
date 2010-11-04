/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.gardener.versioning;

import java.io.File;
import java.util.*;

/**
 *
 * @author Evaldo de Oliveira
 */
public class ConfigurationItem {

    String nameConfigurationItem;

    /**
    *
    * @param serv
    * @param vers
    * @param fileSource
    * @param fileName
    */
   public void createConfigurationItem(String project
                      , String user
                      , String date
                      , String message
                      , String path
                      , String nameConfigurationItem)
       {
            Version vers = new Version(); 
            vers.createVersion(nameConfigurationItem, project, user, date, message, path);
       }

   /**
    *
    * @param serv
    * @param vers
    * @return
    */   

   public String nextVersionItem(String project, String itemName)
   {

       int currentVersion;
       int nextVersion;

       Version vers = new Version();

       currentVersion = Integer.parseInt(vers.currentVersion(project, itemName)) + 1;
       nextVersion = currentVersion + 1;

       return Integer.toString(nextVersion);

   }

   public String currentVersionItem(String project, String itemName)
   {

       Version vers = new Version();
       return vers.currentVersion(project, itemName);

   }

}
