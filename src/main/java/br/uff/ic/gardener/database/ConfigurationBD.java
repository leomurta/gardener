/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.gardener.database;

/**
 *
 * @author Evaldo de Oliveira
 */
public class ConfigurationBD {

    private int port;
    private String server;

    public ConfigurationBD(int port, String server){

        this.setPort(port);
        this.setServer(server);


    }

    public void setPort(int port){

        this.port = port;

    }

    public int getPort(){

        return this.port;

    }

     public void setServer(String server){

        this.server = server;

    }

    public String getServer(){

        return this.server;

    }

}
