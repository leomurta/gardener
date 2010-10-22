/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.gardener.server;

import br.uff.ic.gardener.versioning.*;

/**
 *
 * @author Evaldo de Oliveira
 */
public class Project {
    private String projectName;

    public Project(String projectName)
    {
        this.setProject(projectName);
    }

    /**
     *
     * @param serv
     */
    public void createProject(Project proj)
   {
        Revision revision = new Revision();
        revision.createRevisionProject(proj);
   }
     /**
    *
    * @param project
    */
   public void setProject(String projectName)
   {
       this.projectName = projectName;
   }

   /**
    *
    * @return
    */
   public String getProject()
   {
       return projectName;
   }

}
