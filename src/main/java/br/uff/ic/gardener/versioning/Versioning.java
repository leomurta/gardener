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
   
   public String stgRevisao;
   public String stgData;
   public String stgComentario;
   public String stgUsuario;
   public String stgCaminho;

   public String stgServer;
   public int intPort;
   public String stgProject;
   
   /**
    *
    * @param stgProject
    */
   public void createRevisionProject(String stgProject)
   {

       Database db = new Database();
       db.createDirRepository(stgProject);

   }
    
   /**
    *
    * @param stgProject
    */
   public void createRevision(String stgProject)
    {

       Database db = new Database();                    

       int intRevision = db.lastRevision(stgProject);

       intRevision = intRevision + 1;

       db.intPort = this.intPort;
       db.stgServer = this.stgServer;
       db.stgProject = this.stgProject;

       db.stgRevisao = Integer.toString(intRevision);       
       db.stgComentario = this.stgComentario;
       db.stgData = this.stgData;
       db.stgUsuario = this.stgUsuario;

       db.storeDirRepository(stgProject,  intRevision);
       db.updateRevision(stgProject, intRevision);
     }          

   /**
    *
    * @param stgProject
    * @param stgFileSource
    * @param stgfileName
    */
   public void createFileRevision(String stgProject, String stgFileSource, String stgfileName)
   {
       Database db = new Database();
       int intRevision = db.lastRevision(stgProject);

       db.storeFileRepository(stgProject,  intRevision, stgFileSource, stgfileName);

   }

   /**
    *
    * @param stgProject
    * @param intRevision
    * @return
    */
   public File[] listRevisionFiles(String stgProject, int intRevision)
   {

       Database db = new Database();
       return db.listFiles(stgProject, intRevision);

   }

   /**
    *
    * @param stgProject
    * @return
    */
   public int lastRevision(String stgProject)
   {

       Database db = new Database();
       return db.lastRevision(stgProject);

   }

   /**
    *
    * @param stgProject
    * @return
    */
   public int nextRevision(String stgProject)
   {

       Database db = new Database();
       return db.lastRevision(stgProject) + 1;

   }

   /**
    *
    * @return
    */
   public ArrayList metadataRevision()
   {

       Database db = new Database();

       db.intPort = this.intPort;
       db.stgServer = this.stgServer;
       db.stgProject = this.stgProject;
       db.stgRevisao = this.stgRevisao;

       return db.queryMetadata();

   }

}
