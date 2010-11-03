package br.uff.ic.gardener.database;

import br.uff.ic.gardener.server.*;
import br.uff.ic.gardener.versioning.*;

import com.mongodb.*;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import java.io.*;
import java.io.FileInputStream.*;
import java.io.DataInputStream.*;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

/**
 * Esta classe permite fornecer métodos para acesso e manutenção da
 * base de dados do Sistema de Controle de Versão Gardener.
 * O Gardener é resultado de um trabalho da disciplina de Laboratório de
 * Gerencia de Configuração.
 *
 * Criação em 12 de Outubro de 2010.
 *
 * @author Alessandreia Marta de Oliveira
 *
 */
public class Database {

    private DB conn;

    /**
     * Verifica sistema operacional para definir raiz do projeto
     *
     * @return      retorna a raiz do projeto, de acordo com o sistema operacional
     */
    private String getRoot()
    {
           String os = System.getProperty("os.name");
           if (os.contains("Windows")) {
               return "c:/Gardener/";
           } else {
               return "/Gardener/";
           }
    }

    /**
     * cria a conexao com o BD
     *
     * @param serv
     * @return      retorna a conexao com o BD
     */
    public DB Connection(ConfigurationServer serv, Project proj)
    {
            try
		{

                    Mongo connect = new Mongo(serv.getServer(), serv.getPort());
                    conn = connect.getDB(proj.getProject());
                    return conn;

		}
		catch(Exception err)
        	{
                    err.getMessage();
                    return null;
                }
    }

    /**
     * Cria o diretorio do Repositorio
     *
     * @param serv
     */
    public void createDirRepository(Project proj)
    {
       File root =  new File(getRoot());
       if (!root.exists()) root.mkdir();

       File dir = new File(getRoot() + proj.getProject());
       dir.mkdir();

       try{

            FileOutputStream newFile = new FileOutputStream(getRoot() + proj.getProject() + "/configVersion.conf");
            newFile.close();

            FileReader fr = new FileReader(getRoot() + proj.getProject() + "/configVersion.conf");
            BufferedReader buff = new BufferedReader(fr);

            buff.close();

            FileWriter fw = new FileWriter(getRoot() + proj.getProject() + "/configVersion.conf", false);
            fw.write("0");//First Revision
            fw.close();

        }catch(Exception err)
            {
              System.out.println("Erro: " + err.getMessage());
            }

    }

    /**
     * Cria o diretorio da revisao e armazena os metadados no Diretorio do Repositorio de uma determinada versao
     *
     * @param serv
     * @param vers
     */
    public void storeDirRepository(ConfigurationServer serv, Project proj, Version vers)
    {

        try
        {

            boolean dir = new File(getRoot() + proj.getProject() + "/" + vers.getRevisionNumber()).mkdir();
            if (dir == true) {

                this.storeMetadata(serv, vers, proj);

            }

        }catch(Exception err)
         {
             System.out.println("Erro: " + err.getMessage());
         }

    }

    /**
     * Armazena um arquivo de revisao em uma determinada versao de um projeto
     *
     * @param serv
     * @param vers
     * @param stgFileSource arquivo a ser armazenado
     * @param fileName      nome do arquivo armazenado
     */
    public void storeFileRepository(Version vers
                                  , Project proj
                                  , String stgFileSource
                                  , String fileName)
    {
        //streams
        FileInputStream source;
        FileOutputStream target;

        FileChannel fcSource;
        FileChannel fcTarget;

        try
        {
            source = new FileInputStream(stgFileSource);
            target = new FileOutputStream(getRoot() + proj.getProject() + "/" + vers.getRevisionNumber() + "/" + fileName);

            fcSource  = source.getChannel();
            fcTarget = target.getChannel();

            fcSource.transferTo(0, fcSource.size(), fcTarget);

         }catch(Exception err)
         {
             System.out.println("Erro: " + err.getMessage());
         }

    }

    /**
     * Armazena o metadado no BD
     */
    private void storeMetadata(ConfigurationServer serv, Version vers, Project proj)
    {

        this.conn =  Connection(serv, proj);
        DBCollection collection = this.conn.getCollection(proj.getProject());

        BasicDBObject revision = new BasicDBObject();

        revision.put("revision", vers.getRevisionNumber());
        revision.put("date", vers.getDate());
        revision.put("messagelog", vers.getMessageLog());
        revision.put("user", vers.getUser());
        revision.put("path", getRoot() + proj.getProject() + "/" + vers.getRevisionNumber());

        collection.insert(revision);

    }

    /**
     * Consulta o metadado
     *
     * @param serv
     * @param vers
     * @return  retorna o metadado do projeto
     */
    public ArrayList queryMetadata(ConfigurationServer serv, Project proj, Version vers)
    {

        this.conn =  Connection(serv, proj);
        DBCollection collection = this.conn.getCollection(proj.getProject());

        BasicDBObject revision = new BasicDBObject();
        revision.put("revision", vers.getRevisionNumber());

        DBCursor resultCollection = collection.find();
        resultCollection = collection.find(revision);

        ArrayList resultQuery = new ArrayList();

        while(resultCollection.hasNext()) {

            DBObject resultCollectionAux;
            resultCollectionAux = resultCollection.next();

            String user = resultCollectionAux.get("user").toString();
            String dt = resultCollectionAux.get("date").toString();
            String mensage = resultCollectionAux.get("messagelog").toString();
            String path = resultCollectionAux.get("path").toString();

            resultQuery.add(user);
            resultQuery.add(dt);
            resultQuery.add(mensage);
            resultQuery.add(path);

        }

        return resultQuery;

    }

    /**
     * Atualiza revisao de uma revisao de um projeto
     *
     * @param serv
     * @param vers
     */
    public void updateRevision(Project proj, Version vers)
    {
        try
        {
            FileWriter fw = new FileWriter(getRoot() + proj.getProject() + "/configVersion.conf", false);
            fw.write(vers.getRevisionNumber());

            fw.close();

         }
         catch(Exception err)
         {
             System.out.println("Erro: " + err.getMessage());
         }

    }

    /**
     * retorna a ultima revisao de um projeto
     *
     * @param serv
     * @return              numero da revisao
     */
    public int lastRevision(Project proj)
    {
        try
        {

            int tempRevision;
            FileReader fr = new FileReader(getRoot() + proj.getProject() + "/configVersion.conf");
            BufferedReader buff = new BufferedReader(fr);

            tempRevision = Integer.parseInt(buff.readLine());

            buff.close();

            return tempRevision;

        }
         catch(Exception err)
         {
             return -1;
         }

    }

    /**
     * Lista arquivos de uma revisao de um projeto
     *
     * @param pServ
     * @param pVers
     * @return              lista de arquivos
     */
    public File[] listFiles(Project proj, Version vers)
    {
            File dir = new File(getRoot() + proj.getProject() + "/" + vers.getRevisionNumber());
            File[] listFile = dir.listFiles();

            return listFile;
    }
 }