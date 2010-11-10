/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.gardener.versioning;

/**
 *
 * @author Evaldo de Oliveira
 */
public class User {

    private String userName;

    public User(String userName)
    {
        this.setUserName(userName);
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return this.userName;
    }

}