/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.gardener.versioning;

/**
 *
 * @author Evaldo de Oliveira
 */
public class Project {

    private String nameProject;

    public Project(String nameProject)
    {
        this.setNameProject(nameProject);
    }

    public String getNameProject(){
        return this.nameProject;
    }

    public void setNameProject(String nameProject){
        this.nameProject = nameProject;
    }

}
