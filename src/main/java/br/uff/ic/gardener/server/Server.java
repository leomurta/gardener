package br.uff.ic.gardener.server;

import br.uff.ic.gardener.versioning.Versioning;

import java.util.*;

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


public class Server {  

    private int port;
    private String server;
    private String project;

   public void createProject(String project, String server, int port)
   {
        Versioning revision = new Versioning();

        this.setPort(port);
        this.setServer(server);
        this.setProject(project);

        revision.createRevisionProject(project);
   }

   public void ckeckinRevision(String project
                              , int port
                              , String server
                              , String messageLog
                              , String date
                              , String user
                              , String fileSource
                              , String fileName)
    {
        this.setProject(project);
        this.setPort(port);
        this.setServer(server);

        Versioning revision = new Versioning();
        
        revision.setMessageLog(messageLog);
        revision.setDate(date);
        revision.setUser(user);

        revision.createRevision(project);
        revision.createFileRevision(project, fileSource, fileName);
    }

   public ArrayList ckeckoutRevision(int port, String server, String project, String revisionNumber)
   {      

       this.setPort(port);
       this.setProject(project);
       this.setServer(server);

       Versioning revision = new Versioning();
       revision.setRevisionNumber(revisionNumber);

       return revision.metadataRevision(project);

   }
   
   public void setPort(int port)
   {
       this.port = port;
   }

   public int getPort()
   {
       return port;
   }

   public void setServer(String server)
   {
       this.server = server;
   }

   public String getServer()
   {
       return server;
   }

   public void setProject(String project)
   {
       this.project = project;
   }

   public String getProject()
   {
       return project;
   }

}
