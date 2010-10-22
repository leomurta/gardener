package br.uff.ic.gardener.server;

/**
 *
 * @author Evaldo de Oliveira
 */

/*
 * Esta classe permite fornecer métodos para o Servidor
 * do Sistema de Controle de Versão Gardener.
 * O Gardener é resultado de um trabalho da disciplina de Laboratório de
 * Gerencia de Configuração.
 * Esta disciplina está sendo cursada no Instituto de Computação da UFF,
 * e faz parte do conjunto de disciplinas a serem cursadas no Doutorado em Computação.
 * O professor da disciplina é o Leonardo Murta.
 *
 * Criação em 11 de Outubro de 2010.
 *
 *
 */


public class ConfigurationServer {

    private int port;
    private String serverName;

    public ConfigurationServer(int port, String serverName){

        this.setPort(port);
        this.setServer(serverName);

    }

   
   
   /**
    *
    * @param port
    */
   public void setPort(int port)
   {
       this.port = port;
   }

   /**
    *
    * @return
    */
   public int getPort()
   {
       return port;
   }

   /**
    *
    * @param server
    */
   public void setServer(String serverName)
   {
       this.serverName = serverName;
   }

   /**
    *
    * @return
    */
   public String getServer()
   {
       return serverName;
   }  

}
