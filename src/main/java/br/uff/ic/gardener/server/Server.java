package br.uff.ic.gardener.versioning;

import br.uff.ic.gardener.database.Database;
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

    int intPort = 27017;
    String stgServer = "localhost";

   public void createProject(String stgProject)
   {
        Versioning revision = new Versioning();
        revision.createRevisionProject(stgProject);
   }

   public void ckeckinRevision(String stgProject                              
                              , String stgComentario
                              , String stgData
                              , String stgUsuario
                              , String stgFileSource
                              , String stgFileName)
    {
        Versioning revision = new Versioning();

        revision.intPort = this.intPort;
        revision.stgServer = this.stgServer;
        revision.stgProject = stgProject;
        
        revision.stgComentario = stgComentario;
        revision.stgData = stgData;
        revision.stgUsuario = stgUsuario;

        revision.createRevision(stgProject);
        revision.createFileRevision(stgProject, stgFileSource, stgFileName);
    }

   public ArrayList ckeckoutRevision(String stgProject, String stgRevisao, String stgCaminhoDestino)
    {
       Versioning revision = new Versioning();

       revision.intPort = this.intPort;
       revision.stgServer = this.stgServer;
       revision.stgProject = stgProject;
       revision.stgRevisao = stgRevisao;

       return revision.metadataRevision();

    }
}
