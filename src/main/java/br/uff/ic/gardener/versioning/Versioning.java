package br.uff.ic.gardener.versioning;

import br.uff.ic.gardener.database.Database;
import java.io.File;
import java.util.*;

/**
 *
 * @author Evaldo de Oliveira
 */

/*
 * Esta classe permite fornecer métodos para o versionamento
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

public class Versioning {
   
   private String revisionNumber;
   private String date;
   private String messageLog;
   private String user;   
   
   /**
    *
    * @param project
    */
   public void createRevisionProject(String project)
   {

       Database db = new Database();
       db.createDirRepository(project);

   }
    
   /**
    *
    * @param project
    */
   public void createRevision(String project)
    {

       Database db = new Database();

       int revision = db.lastRevision(project);
       revision = revision + 1;       
       this.setRevisionNumber(Integer.toString(revision));
       
       db.storeDirRepository(project,  revision);
       db.updateRevision(project, revision);
     }          

   /**
    *
    * @param project
    * @param fileSource
    * @param fileName
    */
   public void createFileRevision(String project, String fileSource, String fileName)
   {
       Database db = new Database();
       int revision = db.lastRevision(project);

       db.storeFileRepository(project,  revision, fileSource, fileName);

   }

   /**
    *
    * @param project
    * @param revisionNumber
    * @return
    */
   public File[] listRevisionFiles(String project, int revision)
   {

       Database db = new Database();
       return db.listFiles(project, revision);

   }

   /**
    *
    * @param project
    * @return
    */
   public int lastRevision(String project)
   {

       Database db = new Database();
       return db.lastRevision(project);

   }

   /**
    *
    * @param project
    * @return
    */
   public int nextRevision(String project)
   {

       Database db = new Database();
       return db.lastRevision(project) + 1;

   }

   /**
    *
    * @return
    */
   public ArrayList metadataRevision(String project)
   {

       Database db = new Database();
       return db.queryMetadata(project);

   }

   public void setRevisionNumber(String revisionNumber)
   {
       this.revisionNumber = revisionNumber;
   }

   public String getRevisionNumber()
   {
        return this.revisionNumber;
   }

   public void setDate(String date)
   {
       this.date = date;
   }

   public String getDate()
   {
       return this.date;
   }

   public void setMessageLog(String messageLog)
   {
       this.messageLog = messageLog;
   }

   public String getMessageLog()
   {
       return this.messageLog;
   }

   public void setUser(String user)
   {
       this.user = user;
   }

   public String getUser()
   {
       return this.user;
   }


}




