
package br.uff.ic.gardener.versioning;

import java.io.*;


/**
 *
 * @author Evaldo de Oliveira
 */
public class Item {

    private String nameItem;
    private InputStream item;
    public Project projectItem;

    /**
    *
    * @param serv
    * @param vers
    * @param fileSource
    * @param fileName
    */
   public void createItem(String project
                      , int currentVersion
                      , int nextVersion                      
                      , String user
                      , String date
                      , String message                      
                      , String nameConfigurationItem)
       {
            Version vers = new Version();
            User userVers = new User(user);
            Transaction trans = new Transaction(message, date, "ci");

            projectItem = new Project(project);

            vers.createVersion(nameConfigurationItem
                             , projectItem
                             , currentVersion
                             , nextVersion
                             , nameConfigurationItem
                             , userVers
                             , trans);
       }

   /**
    *
    * @param serv
    * @param vers
    * @return
    */   

   public String nextVersionItem(String projectItem)
   {

       int currentVersion;
       int nextVersion;

       Version vers = new Version();

       currentVersion = Integer.parseInt(vers.getCurrentVersionProject(projectItem)) + 1;
       nextVersion = currentVersion + 1;

       return Integer.toString(nextVersion);

   }

   public String currentVersionItem(String projectItem)
   {

       Version vers = new Version();
       return vers.getCurrentVersionProject(projectItem);

   }

   public void setNameItem(String nameItem)
   {

       this.nameItem = nameItem;

   }

   public String getNameItem()
   {

       return this.nameItem;

   }


   public void setItemAsInputStream(InputStream item) {
          this.item = item;
   }

   public InputStream getItemAsInputStream() {
            return item;
   }

}
