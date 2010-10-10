package Database;

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

/*
 * Esta classe permite fornecer métodos para acesso e manutenção da
 * base de dados do Sistema de Controle de Versão Gardener.
 * O Gardener é resultado de um trabalho da disciplina de Laboratório de
 * Gerencia de Configuração.
 * Esta disciplina está sendo cursada no Instituto de Computação da UFF,
 * e faz parte do conjunto de disciplinas a serem cursadas no Doutorado em Computação.
 * O professor da disciplina é o Leonardo Murta.
 *
 * Criação em 10 de Outubro de 2010.
 *
 * @author Alessandreia
 *
 */
public class Database {

    //Atributos para conexao
    public String stgServer;
    public int intPort;
    public String stgProject;
    public DB conn;

    //Atributos para inserção dos dados
    public String stgRevisao;
    public String stgData;
    public String stgComentario;
    public String stgUsuario;
    public String stgCaminho;
    
    public DB Connection()
    {
            try
		{

                    Mongo connect = new Mongo(this.stgServer, this.intPort);
                    conn = connect.getDB( this.stgProject );

                    return conn;

		}
		catch(Exception err)
        	{
                    err.getMessage();
                    return null;
                }
    }

    public int lastRevision(String stgProject)
    {
        try
        {

            int tempRevision;

            FileReader fr = new FileReader("c:/" + stgProject + "/configVersion.conf");
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

    public void storeMetadata()
    {

        conn =  Connection();
        DBCollection collection = conn.getCollection(this.stgProject);

        BasicDBObject revision = new BasicDBObject();

        revision.put("revisao", this.stgRevisao);
        revision.put("data", this.stgData);
        revision.put("comentario", this.stgComentario);
        revision.put("usuario", this.stgUsuario);
        revision.put("caminho", "c:/" + this.stgProject + "/" + this.stgRevisao);

        collection.insert(revision);

    }

    public void updateRevision(String stgProject, int intRevision)
    {
        try
        {
            FileWriter fw = new FileWriter("c:/" + stgProject + "/configVersion.conf", false);
            fw.write(Integer.toString(intRevision));

            fw.close();            

         }
         catch(Exception err)
         {
             System.out.println("Erro: " + err.getMessage());
         }

    }

    public void createDirRepository(String stgProject)
    {

       File dir = new File("c:/" + stgProject);
       dir.mkdir();

       try{

            FileOutputStream newFile = new FileOutputStream("c:/" + stgProject + "/configVersion.conf");
            newFile.close();

            FileReader fr = new FileReader("c:/" + stgProject + "/configVersion.conf");
            BufferedReader buff = new BufferedReader(fr);

            buff.close();

            FileWriter fw = new FileWriter("c:/" + stgProject + "/configVersion.conf", false);
            fw.write("0");//First Revision
            fw.close();

        }catch(Exception err)
            {
              System.out.println("Erro: " + err.getMessage());
            }

    }

    public File[] listFiles(String stgProject, int intRevision)
    {
            File dir = new File("c:/" + stgProject + "/" + intRevision);
            File[] listFile = dir.listFiles();

            return listFile;

    }

    public void storeDirRepository(String stgProject, int intRevision)
    {        

        try
        {

            boolean dir = new File("c:/" + stgProject + "/" + intRevision).mkdir();
            if (dir == true) {
     
                //GridFS arqRevisao = new GridFS(conn);
                //arqRevisao.createFile(arqOrigem, valorRevisaoS).save();
                
                this.storeMetadata();
                
            
            }            

        }catch(Exception err)
         {
             System.out.println("Erro: " + err.getMessage());
         }

    }

    public void storeFileRepository(String stgProject, int intRevision, String stgFileSource, String fileName)
    {
        //streams
        FileInputStream source;
        FileOutputStream target;

        FileChannel fcSource;
        FileChannel fcTarget;

        try
        {
            source = new FileInputStream(stgFileSource);
            target = new FileOutputStream("c:/" + stgProject + "/" + intRevision + "/" + fileName);

            fcSource  = source.getChannel();
            fcTarget = target.getChannel();           

            fcSource.transferTo(0, fcSource.size(), fcTarget);

         }catch(Exception err)
         {
             System.out.println("Erro: " + err.getMessage());
         }

    }

    public ArrayList queryMetadata()
    {

        conn =  Connection();
        DBCollection collection = conn.getCollection(this.stgProject);

        BasicDBObject revision = new BasicDBObject();
        revision.put("revisao", this.stgRevisao);

        DBCursor resultCollection = collection.find();
        resultCollection = collection.find(revision);

        ArrayList resultQuery = new ArrayList();

        while(resultCollection.hasNext()) {

            DBObject resultCollectionAux;
            resultCollectionAux = resultCollection.next();
                   
            String user = resultCollectionAux.get("usuario").toString();
            String dt = resultCollectionAux.get("data").toString();
            String mensage = resultCollectionAux.get("comentario").toString();
            String path = resultCollectionAux.get("caminho").toString();

            resultQuery.add(user);
            resultQuery.add(dt);
            resultQuery.add(mensage);
            resultQuery.add(path);            
            
        }

        return resultQuery;

    }

 }


