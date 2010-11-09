/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.gardener.database;

/**
 *
 * @author Evaldo de Oliveira
 */
public class Repository {

    private String repository;

    public Repository(String repository){

        this.setRepository(repository);

    }

    public void setRepository(String repository)
    {
        this.repository = repository;
    }

    public String getRepository()
    {
        return this.repository;
    }

}
