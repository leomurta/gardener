package br.uff.ic.gardener.versioning;

import br.uff.ic.gardener.database.Database;
import br.uff.ic.gardener.server.*;

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

public class Revision {
   
   private String revisionNumber;
   private String date;
   private String messageLog;
   private String user;   
   
   /**
    *
    * @param serv
    */
   public void createRevisionProject(Project proj)
   {

       Database db = new Database();
       db.createDirRepository(proj);

   }      

  
   /**
    *
    * @param revisionNumber
    */
   public void setRevisionNumber(String revisionNumber)
   {
       this.revisionNumber = revisionNumber;
   }

   /**
    *
    * @return
    */
   public String getRevisionNumber()
   {
        return this.revisionNumber;
   }

   /**
    *
    * @param date
    */
   public void setDate(String date)
   {
       this.date = date;
   }

   /**
    *
    * @return
    */
   public String getDate()
   {
       return this.date;
   }

   /**
    *
    * @param messageLog
    */
   public void setMessageLog(String messageLog)
   {
       this.messageLog = messageLog;
   }

   /**
    *
    * @return
    */
   public String getMessageLog()
   {
       return this.messageLog;
   }

   /**
    *
    * @param user
    */
   public void setUser(String user)
   {
       this.user = user;
   }

   /**
    *
    * @return
    */
   public String getUser()
   {
       return this.user;
   }


}




